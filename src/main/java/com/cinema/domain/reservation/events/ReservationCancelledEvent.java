package com.cinema.domain.reservation.events;

import com.cinema.domain.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ReservationCancelledEvent(UUID reservationId, UUID userId, String reason, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "ReservationCancelledEvent"; }
}
