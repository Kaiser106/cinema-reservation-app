package com.cinema.infrastructure.persistence.reservation;

import com.cinema.domain.reservation.repository.SeatAvailabilityRepository;
import com.cinema.infrastructure.persistence.hall.SeatJpaRepository;
import com.cinema.infrastructure.persistence.session.SessionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SeatAvailabilityAdapter implements SeatAvailabilityRepository {
    private final SessionJpaRepository sessionJpaRepository;
    private final SeatJpaRepository seatJpaRepository;
    private final ReservationSeatJpaRepository reservationSeatJpaRepository;

    public SeatAvailabilityAdapter(SessionJpaRepository sessionJpaRepository, SeatJpaRepository seatJpaRepository, ReservationSeatJpaRepository reservationSeatJpaRepository) {
        this.sessionJpaRepository = sessionJpaRepository;
        this.seatJpaRepository = seatJpaRepository;
        this.reservationSeatJpaRepository = reservationSeatJpaRepository;
    }

    @Override
    public void lockSessionAndSeats(UUID sessionId, List<Long> seatIds) {
        sessionJpaRepository.findByIdForUpdate(sessionId).orElseThrow();
        seatJpaRepository.lockByIds(seatIds);
    }

    @Override
    public boolean hasReservedSeat(UUID sessionId, List<Long> seatIds) {
        return reservationSeatJpaRepository.existsReservedSeats(sessionId, seatIds);
    }

    @Override
    public List<Long> findAvailableSeatIds(UUID sessionId) {
        var session = sessionJpaRepository.findById(sessionId).orElseThrow();
        var all = seatJpaRepository.findByHallId(session.getHallId()).stream().map(s -> s.getId()).toList();
        var reserved = reservationSeatJpaRepository.findReservedSeatIds(sessionId);
        List<Long> available = new ArrayList<>(all);
        available.removeAll(reserved);
        return available;
    }
}
