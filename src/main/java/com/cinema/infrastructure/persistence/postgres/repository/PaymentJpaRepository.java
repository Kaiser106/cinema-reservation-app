package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Optional<PaymentEntity> findByIdempotencyKey(String idempotencyKey);
    long countByReservationId(UUID reservationId);
}
