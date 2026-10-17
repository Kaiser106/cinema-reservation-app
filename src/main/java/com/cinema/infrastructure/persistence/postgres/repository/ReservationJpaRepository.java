package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {
    Page<ReservationEntity> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    @Query("select r from ReservationEntity r where r.status='PENDING' and r.expiresAt < :now")
    List<ReservationEntity> findExpiredPending(Instant now, Pageable pageable);
}
