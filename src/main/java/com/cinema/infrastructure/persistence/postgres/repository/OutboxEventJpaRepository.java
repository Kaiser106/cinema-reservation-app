package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, UUID> {
    @Query(value = "select * from outbox_events oe where oe.status='PENDING' and (oe.next_attempt_at is null or oe.next_attempt_at <= now()) order by oe.created_at asc limit :limit for update skip locked", nativeQuery = true)
    List<OutboxEventEntity> claimBatch(@Param("limit") int limit);

    @Query("select o from OutboxEventEntity o where o.status='DEAD' order by o.createdAt asc")
    List<OutboxEventEntity> findDeadLetter();
}
