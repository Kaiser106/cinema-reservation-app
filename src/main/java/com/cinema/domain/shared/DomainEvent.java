package com.cinema.domain.shared;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
    String eventType();
}
