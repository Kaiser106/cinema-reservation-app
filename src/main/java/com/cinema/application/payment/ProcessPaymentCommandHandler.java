package com.cinema.application.payment;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.payment.commands.ProcessPaymentCommand;
import com.cinema.application.port.*;
import com.cinema.domain.repository.*;
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
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayPort paymentGatewayPort;
    private final OutboxRepository outboxRepository;
    private final ConfirmReservationCommandHandler confirmReservationCommandHandler;

    public ProcessPaymentCommandHandler(ReservationRepository reservationRepository, PaymentRepository paymentRepository,
                                        PaymentGatewayPort paymentGatewayPort, OutboxRepository outboxRepository,
                                        ConfirmReservationCommandHandler confirmReservationCommandHandler) {
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.paymentGatewayPort = paymentGatewayPort;
        this.outboxRepository = outboxRepository;
        this.confirmReservationCommandHandler = confirmReservationCommandHandler;
    }

    @Override
    @Transactional
    public UUID handle(ProcessPaymentCommand command) {
        var existing = paymentRepository.findByIdempotencyKey(command.idempotencyKey());
        if (existing.isPresent()) {
            Payment previous = existing.get();
            // Same key reused for a different reservation/user payload -> reject instead of silently replaying.
            if (!previous.getReservationId().equals(command.reservationId())
                    || previous.getAmount().toBigDecimal().compareTo(command.amount()) != 0) {
                throw new BusinessException(ErrorCodes.PAYMENT_IDEMPOTENT_REPLAY, "error.payment.idempotencyKeyConflict");
            }
            if (previous.getStatus() == PaymentStatus.FAILED) {
                throw new BusinessException(ErrorCodes.PAYMENT_FAILED, "error.payment.failed");
            }
            return previous.id();
        }

        Reservation reservation = reservationRepository.findById(command.reservationId())
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

        Payment saved = paymentRepository.save(payment);
        outboxRepository.storeAll(saved.pullDomainEvents());

        if (saved.getStatus() == PaymentStatus.FAILED) {
            throw new BusinessException(ErrorCodes.PAYMENT_FAILED, "error.payment.failed");
        }

        return saved.id();
    }
}
