package com.cinema.infrastructure.outbox;

import com.cinema.domain.common.DomainEvent;
import com.cinema.infrastructure.persistence.postgres.entity.OutboxEventEntity;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class OutboxEventMapper {
    private final ObjectMapper objectMapper;

    public OutboxEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public OutboxEventEntity toEntity(DomainEvent event) {
        OutboxEventEntity e = new OutboxEventEntity();
        e.setId(UUID.randomUUID());
        e.setEventType(event.eventType());
        try {
            e.setPayload(objectMapper.writeValueAsString(event));
        } catch (JacksonException ex) {
            throw new IllegalStateException(ex);
        }
        e.setStatus("PENDING");
        e.setRetryCount(0);
        e.setCreatedAt(Instant.now());
        return e;
    }
}