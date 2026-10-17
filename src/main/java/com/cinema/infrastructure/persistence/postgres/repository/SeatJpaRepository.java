package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.SeatEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SeatEntity s where s.id in :ids")
    List<SeatEntity> lockByIds(List<Long> ids);

    List<SeatEntity> findByHallId(Long hallId);
}
