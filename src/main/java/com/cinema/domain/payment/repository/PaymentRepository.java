package com.cinema.domain.payment.repository;

import com.cinema.domain.payment.model.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
    long countByReservationId(UUID reservationId);
}
