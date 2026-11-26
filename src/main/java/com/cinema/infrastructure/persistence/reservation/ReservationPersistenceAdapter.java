package com.cinema.infrastructure.persistence.reservation;

import com.cinema.domain.reservation.model.Reservation;
import com.cinema.domain.reservation.model.ReservationSeat;
import com.cinema.domain.reservation.model.ReservationStatus;
import com.cinema.domain.reservation.repository.ReservationRepository;
import com.cinema.domain.shared.vobj.Money;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ReservationPersistenceAdapter implements ReservationRepository {

    private final ReservationJpaRepository repo;

    public ReservationPersistenceAdapter(ReservationJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        // Varsa mevcut (managed) entity'yi yükle, yoksa yeni oluştur
        ReservationEntity e = repo.findById(reservation.id()).orElseGet(() -> {
            ReservationEntity n = new ReservationEntity();
            n.setId(reservation.id());
            return n;
        });

        e.setUserId(reservation.getUserId());
        e.setSessionId(reservation.getSessionId());
        e.setStatus(reservation.getStatus().name());
        e.setTotalPrice(reservation.getTotalPrice().toBigDecimal());
        e.setCreatedAt(reservation.getCreatedAt());
        e.setExpiresAt(reservation.getExpiresAt());

        // Koltukları senkronize et: version bilgisi korunur
        Set<UUID> incomingIds = reservation.getSeats().stream()
                .map(ReservationSeat::getId)
                .collect(Collectors.toSet());
        e.getSeats().removeIf(se -> !incomingIds.contains(se.getId()));

        Map<UUID, ReservationSeatEntity> existing = e.getSeats().stream()
                .collect(Collectors.toMap(ReservationSeatEntity::getId, Function.identity()));

        for (ReservationSeat s : reservation.getSeats()) {
            ReservationSeatEntity se = existing.get(s.getId());
            if (se == null) {
                se = new ReservationSeatEntity();
                se.setId(s.getId());
                se.setReservation(e);
                e.getSeats().add(se);
            }
            se.setSeatId(s.getSeatId());
            se.setSessionId(s.getSessionId());
            se.setPrice(s.getPrice().toBigDecimal());
        }

        repo.save(e);
        return reservation;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Reservation> findById(UUID id) {
        return repo.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Reservation> findByUserId(UUID userId, Pageable pageable) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId, pageable).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findPendingExpired(Instant now, int limit) {
        return repo.findExpiredPending(now, Pageable.ofSize(limit)).stream().map(this::toDomain).toList();
    }

    private Reservation toDomain(ReservationEntity e) {
        var seats = e.getSeats().stream()
                .map(s -> new ReservationSeat(s.getId(), s.getSeatId(), s.getSessionId(), Money.of(s.getPrice())))
                .toList();
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
