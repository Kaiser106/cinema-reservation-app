package com.cinema.domain.user;

import java.time.Instant;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final UserRole role;
    private final Instant createdAt;
    private boolean active;

    public User(UUID id, String email, String passwordHash, UserRole role, Instant createdAt, boolean active) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
        this.active = active;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserRole getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
}
