package com.cinema.infrastructure.persistence.postgres.repository;

import com.cinema.infrastructure.persistence.postgres.entity.ReservationSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ReservationSeatJpaRepository extends JpaRepository<ReservationSeatEntity, UUID> {
    @Query("select count(rs)>0 from ReservationSeatEntity rs join rs.reservation r where rs.sessionId=:sessionId and rs.seatId in :seatIds and r.status in ('PENDING','CONFIRMED')")
    boolean existsReservedSeats(UUID sessionId, List<Long> seatIds);

    @Query("select rs.seatId from ReservationSeatEntity rs join rs.reservation r where rs.sessionId=:sessionId and r.status in ('PENDING','CONFIRMED')")
    List<Long> findReservedSeatIds(UUID sessionId);
}
