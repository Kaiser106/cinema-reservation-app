package com.cinema.infrastructure.outbox;

import com.cinema.application.shared.ports.OutboxRepository;
import com.cinema.domain.shared.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPersistenceAdapter implements OutboxRepository {
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
