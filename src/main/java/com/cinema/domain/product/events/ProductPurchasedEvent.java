package com.cinema.domain.product.events;

import com.cinema.domain.common.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record ProductPurchasedEvent(UUID userId, Long productId, int quantity, Instant occurredAt) implements DomainEvent {
    @Override
    public String eventType() { return "ProductPurchasedEvent"; }
}
