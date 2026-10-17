package com.cinema.outbox;

import com.cinema.infrastructure.outbox.OutboxEventProcessor;
import com.cinema.infrastructure.outbox.OutboxProcessorJob;
import com.cinema.infrastructure.persistence.postgres.entity.OutboxEventEntity;
import com.cinema.infrastructure.persistence.postgres.repository.OutboxEventJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxProcessorTest {
    @Mock OutboxEventJpaRepository repo;
    @Mock OutboxEventProcessor processor;
    @InjectMocks OutboxProcessorJob job;

    @Test
    void shouldMarkProcessed() {
        OutboxEventEntity e = new OutboxEventEntity();
        e.setId(UUID.randomUUID());
        e.setStatus("PENDING");
        e.setPayload("{}");
        e.setEventType("ReservationConfirmedEvent");
        e.setCreatedAt(Instant.now());
        e.setRetryCount(0);

        when(repo.claimBatch(20)).thenReturn(List.of(e));
        when(repo.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        job.processOutbox();
        verify(processor, times(1)).process(any());
        verify(repo, atLeastOnce()).save(any());
    }
}
