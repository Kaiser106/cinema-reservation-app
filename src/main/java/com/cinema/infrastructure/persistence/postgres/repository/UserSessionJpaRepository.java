package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionJpaRepository extends JpaRepository<UserSessionEntity, UUID> {
    Optional<UserSessionEntity> findBySessionToken(String sessionToken);

    default Optional<UserSessionEntity> findValid(String token, Instant now) {
        return findBySessionToken(token).filter(s -> s.getInvalidatedAt() == null && s.getExpiresAt().isAfter(now));
    }
}
