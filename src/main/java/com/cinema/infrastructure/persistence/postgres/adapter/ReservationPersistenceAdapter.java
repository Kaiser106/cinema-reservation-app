package com.cinema.infrastructure.persistence.postgres.adapter;

import com.cinema.application.port.ReservationPort;
import com.cinema.domain.common.Money;
import com.cinema.domain.reservation.Reservation;
import com.cinema.domain.reservation.ReservationSeat;
import com.cinema.domain.reservation.ReservationStatus;
import com.cinema.infrastructure.persistence.postgres.entity.ReservationEntity;
import com.cinema.infrastructure.persistence.postgres.entity.ReservationSeatEntity;
import com.cinema.infrastructure.persistence.postgres.repository.ReservationJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReservationPersistenceAdapter implements ReservationPort {
    private final ReservationJpaRepository repo;

    public ReservationPersistenceAdapter(ReservationJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationEntity e = new ReservationEntity();
        e.setId(reservation.id());
        e.setUserId(reservation.getUserId());
        e.setSessionId(reservation.getSessionId());
        e.setStatus(reservation.getStatus().name());
        e.setTotalPrice(reservation.getTotalPrice().toBigDecimal());
        e.setCreatedAt(reservation.getCreatedAt());
        e.setExpiresAt(reservation.getExpiresAt());

        List<ReservationSeatEntity> seats = reservation.getSeats().stream().map(s -> {
            ReservationSeatEntity se = new ReservationSeatEntity();
            se.setId(s.getId());
            se.setReservation(e);
            se.setSeatId(s.getSeatId());
            se.setSessionId(s.getSessionId());
            se.setPrice(s.getPrice().toBigDecimal());
            return se;
        }).toList();
        e.getSeats().clear();
        e.getSeats().addAll(seats);
        repo.save(e);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Reservation> findByUserId(UUID userId, Pageable pageable) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId, pageable).map(this::toDomain);
    }

    @Override
    public List<Reservation> findPendingExpired(Instant now, int limit) {
        return repo.findExpiredPending(now, Pageable.ofSize(limit)).stream().map(this::toDomain).toList();
    }

    private Reservation toDomain(ReservationEntity e) {
        var seats = e.getSeats().stream().map(s -> new ReservationSeat(s.getId(), s.getSeatId(), s.getSessionId(), Money.of(s.getPrice()))).toList();
        return Reservation.rehydrate(
                e.getId(),
                e.getUserId(),
                e.getSessionId(),
                e.getCreatedAt(),
                e.getExpiresAt(),
                ReservationStatus.valueOf(e.getStatus()),
                Money.of(e.getTotalPrice()),
                seats
        );
    }
}
