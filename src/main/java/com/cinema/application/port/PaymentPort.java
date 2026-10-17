package com.cinema.application.port;

import com.cinema.domain.payment.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentPort {
    Payment save(Payment payment);
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    long countByReservationId(UUID reservationId);
}
