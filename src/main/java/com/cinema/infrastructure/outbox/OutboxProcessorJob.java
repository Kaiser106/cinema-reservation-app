package com.cinema.infrastructure.outbox;

import com.cinema.infrastructure.persistence.postgres.entity.OutboxEventEntity;
import com.cinema.infrastructure.persistence.postgres.repository.OutboxEventJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxProcessorJob {
    private static final Logger log = LoggerFactory.getLogger(OutboxProcessorJob.class);
    private static final int MAX_ATTEMPTS = 5;

    private final OutboxEventJpaRepository repo;
    private final OutboxEventProcessor processor;

    public OutboxProcessorJob(OutboxEventJpaRepository repo, OutboxEventProcessor processor) {
        this.repo = repo;
        this.processor = processor;
    }

    @Scheduled(fixedDelayString = "${app.outbox.fixed-delay-ms:5000}")
    public void processOutbox() {
        List<OutboxEventEntity> batch = claimBatch(20);
        for (OutboxEventEntity e : batch) {
            processSingle(e);
        }
    }

    @Transactional
    public List<OutboxEventEntity> claimBatch(int limit) {
        List<OutboxEventEntity> rows = repo.claimBatch(limit);
        rows.forEach(r -> r.setStatus("PROCESSING"));
        return repo.saveAll(rows);
    }

    @Transactional
    public void processSingle(OutboxEventEntity event) {
        try {
            processor.process(event);
            event.setStatus("PROCESSED");
            event.setProcessedAt(Instant.now());
            event.setLastError(null);
        } catch (Exception ex) {
            int retries = event.getRetryCount() + 1;
            event.setRetryCount(retries);
            event.setLastError(ex.getMessage());
            if (retries >= MAX_ATTEMPTS) {
                event.setStatus("DEAD");
            } else {
                event.setStatus("PENDING");
                event.setNextAttemptAt(Instant.now().plusSeconds((long) Math.pow(2, retries)));
            }
            log.warn("Outbox processing failed eventId={} retries={}", event.getId(), retries);
        }
        repo.save(event);
    }
}
