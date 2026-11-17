package com.cinema.payment;

import com.cinema.application.payment.ProcessPaymentCommandHandler;
import com.cinema.application.payment.commands.ProcessPaymentCommand;
import com.cinema.application.port.*;
import com.cinema.domain.repository.*;
import com.cinema.application.reservation.ConfirmReservationCommandHandler;
import com.cinema.domain.common.Money;
import com.cinema.domain.payment.Payment;
import com.cinema.domain.reservation.Reservation;
import com.cinema.domain.reservation.ReservationSeat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentIdempotencyTest {
    @Mock ReservationRepository reservationRepository;
    @Mock PaymentRepository paymentRepository;
    @Mock PaymentGatewayPort paymentGatewayPort;
    @Mock OutboxRepository outboxRepository;
    @Mock ConfirmReservationCommandHandler confirmReservationCommandHandler;

    ProcessPaymentCommandHandler handler;

    @BeforeEach
    void setup() {
        handler = new ProcessPaymentCommandHandler(reservationRepository, paymentRepository, paymentGatewayPort, outboxRepository, confirmReservationCommandHandler);
    }

    @Test
    void duplicateRequestsMustNotCreateDuplicatePayments() {
        UUID userId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();
        UUID existingPaymentId = UUID.randomUUID();

        Reservation reservation = new Reservation(reservationId, userId, UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(1000));
        reservation.addSeat(new ReservationSeat(UUID.randomUUID(), 1L, reservation.getSessionId(), Money.of(new BigDecimal("50.00"))));

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(paymentGatewayPort.pay(any(), any(), any(), any())).thenReturn(new PaymentGatewayPort.PaymentResult(true, "tx-1", null));

        Payment existing = new Payment(existingPaymentId, reservationId, Money.of(new BigDecimal("50.00")), "CARD", "k1");
        existing.markCompleted("tx-1");

        when(paymentRepository.findByIdempotencyKey("k1"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existing));
        when(paymentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        handler.handle(new ProcessPaymentCommand(reservationId, userId, new BigDecimal("50.00"), "CARD", "k1"));
        handler.handle(new ProcessPaymentCommand(reservationId, userId, new BigDecimal("50.00"), "CARD", "k1"));

        verify(paymentRepository, times(1)).save(any());
        verify(paymentGatewayPort, times(1)).pay(any(), any(), any(), any());
    }
}
