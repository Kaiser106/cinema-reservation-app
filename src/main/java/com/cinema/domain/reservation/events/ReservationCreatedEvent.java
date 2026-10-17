package com.cinema.domain.reservation.events;

import com.cinema.domain.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ReservationCreatedEvent(UUID reservationId, UUID userId, UUID sessionId, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "ReservationCreatedEvent"; }
}
