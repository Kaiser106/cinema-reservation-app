package com.cinema.infrastructure.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Component
public class OutboxProcessorJob {
    private static final Logger log = LoggerFactory.getLogger(OutboxProcessorJob.class);
    static final int MAX_ATTEMPTS = 5;
    static final int BATCH_SIZE = 20;
    static final long LEASE_SECONDS = 60;

    private final OutboxEventJpaRepository repo;
    private final OutboxEventProcessor processor;
    private final TransactionTemplate tx;

    public OutboxProcessorJob(OutboxEventJpaRepository repo, OutboxEventProcessor processor, PlatformTransactionManager txManager) {
        this.repo = repo;
        this.processor = processor;
        this.tx = new TransactionTemplate(txManager);
        this.tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Scheduled(fixedDelayString = "${app.outbox.fixed-delay-ms:5000}")
    public void processOutbox() {
        List<UUID> ids = tx.execute(s -> claimBatch(BATCH_SIZE));
        if (ids == null) return;
        for (UUID id : ids) {
            try {
                tx.executeWithoutResult(s -> processSingle(id));
            } catch (Exception ex) {
                log.error("Outbox event could not be finalized eventId={}", id, ex);
            }
        }
    }

    List<UUID> claimBatch(int limit) {
        List<OutboxEventEntity> rows = repo.claimBatch(limit);
        Instant leaseUntil = Instant.now().plusSeconds(LEASE_SECONDS);
        rows.forEach(r -> { r.setStatus("PROCESSING"); r.setNextAttemptAt(leaseUntil); });
        repo.saveAll(rows);
        return rows.stream().map(OutboxEventEntity::getId).toList();
    }

    void processSingle(UUID id) {
        OutboxEventEntity event = repo.findById(id).orElse(null);
        if (event == null || !"PROCESSING".equals(event.getStatus())) return;
        try {
            processor.process(event);
            event.setStatus("PROCESSED");
            event.setProcessedAt(Instant.now());
            event.setNextAttemptAt(null);
            event.setLastError(null);
        } catch (Exception ex) {
            int retries = event.getRetryCount() + 1;
            event.setRetryCount(retries);
            event.setLastError(ex.getMessage());
            if (retries >= MAX_ATTEMPTS) {
                event.setStatus("DEAD");
                event.setNextAttemptAt(null);
            } else {
                event.setStatus("PENDING");
                event.setNextAttemptAt(Instant.now().plusSeconds((long) Math.pow(2, retries)));
            }
            log.warn("Outbox processing failed eventId={} retries={}", event.getId(), retries);
        }
        repo.save(event);
    }
}
