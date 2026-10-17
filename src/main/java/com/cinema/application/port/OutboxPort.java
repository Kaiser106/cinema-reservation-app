package com.cinema.application.port;

import com.cinema.domain.common.DomainEvent;

import java.util.List;

public interface OutboxPort {
    void storeAll(List<DomainEvent> events);
}
