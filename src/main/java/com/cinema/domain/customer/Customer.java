package com.cinema.domain.customer;

import java.time.Instant;
import java.util.UUID;

public class Customer {
    private final UUID id;
    private final UUID userId;
    private final String fullName;
    private final String phone;
    private final Instant createdAt;

    public Customer(UUID id, UUID userId, String fullName, String phone, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.fullName = fullName;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public Instant getCreatedAt() { return createdAt; }
}
