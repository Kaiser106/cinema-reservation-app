package com.cinema.infrastructure.outbox;

import com.cinema.application.port.OutboxPort;
import com.cinema.domain.common.DomainEvent;
import com.cinema.infrastructure.persistence.postgres.repository.OutboxEventJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPersistenceAdapter implements OutboxPort {
    private final OutboxEventJpaRepository repo;
    private final OutboxEventMapper mapper;

    public OutboxPersistenceAdapter(OutboxEventJpaRepository repo, OutboxEventMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public void storeAll(List<DomainEvent> events) {
        if (events == null || events.isEmpty()) return;
        repo.saveAll(events.stream().map(mapper::toEntity).toList());
    }
}
