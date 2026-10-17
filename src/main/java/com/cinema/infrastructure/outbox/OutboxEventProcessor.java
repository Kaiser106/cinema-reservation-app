package com.cinema.infrastructure.outbox;

import com.cinema.application.port.SmsPort;
import com.cinema.infrastructure.persistence.postgres.entity.OutboxEventEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventProcessor {
    private final ObjectMapper objectMapper;
    private final SmsPort smsPort;

    public OutboxEventProcessor(ObjectMapper objectMapper, SmsPort smsPort) {
        this.objectMapper = objectMapper;
        this.smsPort = smsPort;
    }

    public void process(OutboxEventEntity event) {
        try {
            JsonNode payload = objectMapper.readTree(event.getPayload());
            if ("ReservationConfirmedEvent".equals(event.getEventType())) {
                // In real flow, phone number resolves via customer profile lookup.
                smsPort.send("+900000000000", "Rezervasyonunuz onaylandi: " + payload.get("reservationId").asText());
            }
        } catch (Exception ex) {
            throw new IllegalStateException("outbox.process.error", ex);
        }
    }
}
