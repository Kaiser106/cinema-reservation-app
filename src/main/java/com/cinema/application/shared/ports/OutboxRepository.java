package com.cinema.application.shared.ports;

import com.cinema.domain.shared.DomainEvent;

import java.util.List;

public interface OutboxRepository {
    void storeAll(List<DomainEvent> events);
}
