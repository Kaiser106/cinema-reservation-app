package com.cinema.domain.repository;

import com.cinema.domain.event.DomainEvent;

import java.util.List;

public interface OutboxRepository {
    void storeAll(List<DomainEvent> events);
}
