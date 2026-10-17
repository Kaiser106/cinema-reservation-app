package com.cinema.domain.point.events;

import com.cinema.domain.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record PointsEarnedEvent(UUID userId, int points, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "PointsEarnedEvent"; }
}
