package com.cinema.domain.common;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
    String eventType();
}
