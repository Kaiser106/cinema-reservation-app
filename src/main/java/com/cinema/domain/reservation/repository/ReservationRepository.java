package com.cinema.domain.reservation.repository;

import com.cinema.domain.reservation.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(UUID id);
    Page<Reservation> findByUserId(UUID userId, Pageable pageable);
    List<Reservation> findPendingExpired(Instant now, int limit);
}
