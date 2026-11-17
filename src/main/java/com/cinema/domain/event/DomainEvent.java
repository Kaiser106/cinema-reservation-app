package com.cinema.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
    String eventType();
}
