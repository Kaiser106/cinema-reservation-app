package com.cinema.infrastructure.auth;

import com.cinema.application.port.SessionAuthPort;
import com.cinema.infrastructure.persistence.postgres.entity.UserSessionEntity;
import com.cinema.infrastructure.persistence.postgres.repository.UserSessionJpaRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersistentSessionService implements SessionAuthPort {
    private final UserSessionJpaRepository repo;
    private final SecureRandom secureRandom = new SecureRandom();

    public PersistentSessionService(UserSessionJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public String createSession(UUID userId, String role, String userAgent, String ipAddress, Instant expiresAt) {
        byte[] bytes = new byte[32]; // 256-bit token
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        UserSessionEntity e = new UserSessionEntity();
        e.setId(UUID.randomUUID());
        e.setSessionToken(token);
        e.setUserId(userId);
        e.setRole(role);
        e.setUserAgent(userAgent);
        e.setIpAddress(ipAddress);
        e.setExpiresAt(expiresAt);
        e.setCreatedAt(Instant.now());
        repo.save(e);
        return token;
    }

    @Override
    public Optional<SessionData> findValidSession(String token) {
        return repo.findValid(token, Instant.now()).map(s -> new SessionData(s.getUserId(), s.getRole(), s.getExpiresAt()));
    }

    @Override
    public void invalidate(String token) {
        repo.findBySessionToken(token).ifPresent(s -> {
            s.setInvalidatedAt(Instant.now());
            repo.save(s);
        });
    }
}
