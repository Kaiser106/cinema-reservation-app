package com.cinema.application.port;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface SessionAuthPort {
    String createSession(UUID userId, String role, String userAgent, String ipAddress, Instant expiresAt);
    Optional<SessionData> findValidSession(String token);
    void invalidate(String token);

    record SessionData(UUID userId, String role, Instant expiresAt) {}
}
