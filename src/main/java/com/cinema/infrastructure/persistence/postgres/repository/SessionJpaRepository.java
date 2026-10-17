package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.application.session.dto.SessionDto;
import com.cinema.infrastructure.persistence.postgres.entity.SessionEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SessionJpaRepository extends JpaRepository<SessionEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SessionEntity s where s.id=:id")
    Optional<SessionEntity> findByIdForUpdate(UUID id);

    @Query("select new com.cinema.application.session.dto.SessionDto(s.id,s.movieId,s.hallId,s.startTime,s.endTime,s.price) from SessionEntity s")
    Page<SessionDto> findSessionDtos(Pageable pageable);

    @Query("select new com.cinema.application.session.dto.SessionDto(s.id,s.movieId,s.hallId,s.startTime,s.endTime,s.price) from SessionEntity s where s.id=:id")
    Optional<SessionDto> findSessionDto(UUID id);
}
