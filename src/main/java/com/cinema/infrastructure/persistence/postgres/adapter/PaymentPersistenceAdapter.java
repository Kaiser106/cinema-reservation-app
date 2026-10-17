package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.port.PaymentPort;
import com.cinema.domain.common.Money;
import com.cinema.domain.payment.Payment;
import com.cinema.domain.payment.PaymentStatus;
import com.cinema.infrastructure.persistence.postgres.entity.PaymentEntity;
import com.cinema.infrastructure.persistence.postgres.repository.PaymentJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentPersistenceAdapter implements PaymentPort {
    private final PaymentJpaRepository repo;

    public PaymentPersistenceAdapter(PaymentJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity e = new PaymentEntity();
        e.setId(payment.id());
        e.setReservationId(payment.getReservationId());
        e.setAmount(payment.getAmount().toBigDecimal());
        e.setPaymentMethod(payment.getPaymentMethod());
        e.setStatus(payment.getStatus().name());
        e.setPaidAt(payment.getPaidAt());
        e.setTransactionRef(payment.getTransactionRef());
        e.setIdempotencyKey(payment.getIdempotencyKey());
        e.setCreatedAt(Instant.now());
        repo.save(e);
        return payment;
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        return repo.findByIdempotencyKey(idempotencyKey).map(this::toDomain);
    }

    @Override
    public long countByReservationId(UUID reservationId) {
        return repo.countByReservationId(reservationId);
    }

    private Payment toDomain(PaymentEntity e) {
        Payment p = new Payment(e.getId(), e.getReservationId(), Money.of(e.getAmount()), e.getPaymentMethod(), e.getIdempotencyKey());
        if (PaymentStatus.COMPLETED.name().equals(e.getStatus())) {
            p.markCompleted(e.getTransactionRef());
        } else if (PaymentStatus.FAILED.name().equals(e.getStatus())) {
            p.markFailed("restored");
        }
        p.pullDomainEvents();
        return p;
    }
}
