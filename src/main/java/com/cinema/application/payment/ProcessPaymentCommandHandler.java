package com.cinema.application.payment;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.payment.commands.ProcessPaymentCommand;
import com.cinema.application.port.*;
import com.cinema.application.reservation.commands.ConfirmReservationCommand;
import com.cinema.application.reservation.ConfirmReservationCommandHandler;
import com.cinema.domain.common.Money;
import com.cinema.domain.payment.Payment;
import com.cinema.domain.payment.PaymentStatus;
import com.cinema.domain.reservation.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class ProcessPaymentCommandHandler implements CommandHandler<ProcessPaymentCommand, UUID> {
    private final ReservationPort reservationPort;
    private final PaymentPort paymentPort;
    private final PaymentGatewayPort paymentGatewayPort;
    private final OutboxPort outboxPort;
    private final ConfirmReservationCommandHandler confirmReservationCommandHandler;

    public ProcessPaymentCommandHandler(ReservationPort reservationPort, PaymentPort paymentPort,
                                        PaymentGatewayPort paymentGatewayPort, OutboxPort outboxPort,
                                        ConfirmReservationCommandHandler confirmReservationCommandHandler) {
        this.reservationPort = reservationPort;
        this.paymentPort = paymentPort;
        this.paymentGatewayPort = paymentGatewayPort;
        this.outboxPort = outboxPort;
        this.confirmReservationCommandHandler = confirmReservationCommandHandler;
    }

    @Override
    @Transactional
    public UUID handle(ProcessPaymentCommand command) {
        var existing = paymentPort.findByIdempotencyKey(command.idempotencyKey());
        if (existing.isPresent()) {
            return existing.get().id();
        }

        Reservation reservation = reservationPort.findById(command.reservationId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.RESERVATION_NOT_FOUND, "error.reservation.notFound"));

        if (!reservation.getUserId().equals(command.userId())) {
            throw new BusinessException(ErrorCodes.RESERVATION_ACCESS_DENIED, "error.reservation.accessDenied");
        }

        if (reservation.getTotalPrice().toBigDecimal().compareTo(command.amount()) != 0) {
            throw new BusinessException("PAYMENT_AMOUNT_MISMATCH", "error.payment.amountMismatch");
        }

        Payment payment = new Payment(UUID.randomUUID(), reservation.id(), Money.of(command.amount()), command.paymentMethod(), command.idempotencyKey());
        var result = paymentGatewayPort.pay(command.reservationId(), command.amount(), command.paymentMethod(), command.idempotencyKey());
        if (result.success()) {
            payment.markCompleted(result.transactionRef());
            confirmReservationCommandHandler.handle(new ConfirmReservationCommand(command.reservationId(), command.userId()));
        } else {
            payment.markFailed(result.failureReason());
        }

        Payment saved = paymentPort.save(payment);
        outboxPort.storeAll(saved.pullDomainEvents());

        if (saved.getStatus() == PaymentStatus.FAILED) {
            throw new BusinessException(ErrorCodes.PAYMENT_FAILED, "error.payment.failed");
        }

        return saved.id();
    }
}
