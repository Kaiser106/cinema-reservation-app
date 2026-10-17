package com.cinema.domain.payment;

import com.cinema.domain.common.AggregateRoot;
import com.cinema.domain.common.Money;
import com.cinema.domain.payment.events.PaymentCompletedEvent;
import com.cinema.domain.payment.events.PaymentFailedEvent;

import java.time.Instant;
import java.util.UUID;

public class Payment extends AggregateRoot<UUID> {
    private final UUID id;
    private final UUID reservationId;
    private final Money amount;
    private final String paymentMethod;
    private final String idempotencyKey;
    private String transactionRef;
    private PaymentStatus status;
    private Instant paidAt;

    public Payment(UUID id, UUID reservationId, Money amount, String paymentMethod, String idempotencyKey) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.idempotencyKey = idempotencyKey;
        this.status = PaymentStatus.PENDING;
    }

    public void markCompleted(String transactionRef) {
        if (status == PaymentStatus.COMPLETED) return;
        this.status = PaymentStatus.COMPLETED;
        this.transactionRef = transactionRef;
        this.paidAt = Instant.now();
        registerEvent(new PaymentCompletedEvent(id, reservationId, Instant.now()));
    }

    public void markFailed(String reason) {
        if (status == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("payment.completed.cannot.fail");
        }
        this.status = PaymentStatus.FAILED;
        registerEvent(new PaymentFailedEvent(id, reservationId, reason, Instant.now()));
    }

    @Override
    public UUID id() { return id; }
    public UUID getReservationId() { return reservationId; }
    public Money getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getTransactionRef() { return transactionRef; }
    public PaymentStatus getStatus() { return status; }
    public Instant getPaidAt() { return paidAt; }
}
