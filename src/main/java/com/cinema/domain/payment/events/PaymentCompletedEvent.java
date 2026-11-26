package com.cinema.domain.payment.events;

import com.cinema.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record PaymentCompletedEvent(UUID paymentId, UUID reservationId, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "PaymentCompletedEvent"; }
}
