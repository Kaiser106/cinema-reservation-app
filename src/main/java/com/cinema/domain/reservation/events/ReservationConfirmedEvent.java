package com.cinema.domain.reservation.events;

import com.cinema.domain.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ReservationConfirmedEvent(UUID reservationId, UUID userId, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "ReservationConfirmedEvent"; }
}
