package com.cinema.infrastructure.outbox;

import com.cinema.application.shared.ports.SmsPort;
import com.cinema.infrastructure.persistence.customer.CustomerJpaRepository;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/** Dispatches a single outbox event to its publisher. Exceptions propagate so the job can schedule a retry. */
@Component
public class OutboxEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(OutboxEventProcessor.class);
    private final ObjectMapper objectMapper;
    private final SmsPort smsPort;
    private final CustomerJpaRepository customerRepo;

    public OutboxEventProcessor(ObjectMapper objectMapper, SmsPort smsPort, CustomerJpaRepository customerRepo) {
        this.objectMapper = objectMapper;
        this.smsPort = smsPort;
        this.customerRepo = customerRepo;
    }

    public void process(OutboxEventEntity event) {
        JsonNode payload = objectMapper.readTree(event.getPayload());
        switch (event.getEventType()) {
            case "ReservationConfirmedEvent" -> {
                UUID userId = UUID.fromString(payload.get("userId").asString());
                String phone = customerRepo.findByUserId(userId)
                        .map(c -> c.getPhone())
                        .orElseThrow(() -> new IllegalStateException("outbox.customer.notFound:" + userId));
                smsPort.send(phone, "Rezervasyonunuz onaylandi: " + payload.get("reservationId").asString());
            }
            default -> log.debug("No publisher registered for eventType={}, marking processed", event.getEventType());
        }
    }
}
