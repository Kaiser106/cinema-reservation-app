package com.cinema.domain.payment.events;

import com.cinema.domain.shared.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record PaymentFailedEvent(UUID paymentId, UUID reservationId, String reason, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "PaymentFailedEvent"; }
}
