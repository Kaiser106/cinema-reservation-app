package com.cinema.domain.reservation;

import com.cinema.domain.common.AggregateRoot;
import com.cinema.domain.common.Money;
import com.cinema.domain.reservation.events.ReservationCancelledEvent;
import com.cinema.domain.reservation.events.ReservationConfirmedEvent;
import com.cinema.domain.reservation.events.ReservationCreatedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Reservation aggregate root owns reservation lifecycle and seat lines.
 * It is intentionally pure Java: no framework annotations to keep domain portable and testable.
 */
public class Reservation extends AggregateRoot<UUID> {
    private final UUID id;
    private final UUID userId;
    private final UUID sessionId;
    private final Instant createdAt;
    private Instant expiresAt;
    private ReservationStatus status;
    private Money totalPrice;
    private final List<ReservationSeat> seats;

    public Reservation(UUID id, UUID userId, UUID sessionId, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = ReservationStatus.PENDING;
        this.totalPrice = Money.of(java.math.BigDecimal.ZERO);
        this.seats = new ArrayList<>();
        registerEvent(new ReservationCreatedEvent(id, userId, sessionId, Instant.now()));
    }

    private Reservation(UUID id, UUID userId, UUID sessionId, Instant createdAt, Instant expiresAt,
                        ReservationStatus status, Money totalPrice, List<ReservationSeat> seats) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.seats = new ArrayList<>(seats);
    }

    public static Reservation rehydrate(UUID id, UUID userId, UUID sessionId, Instant createdAt, Instant expiresAt,
                                        ReservationStatus status, Money totalPrice, List<ReservationSeat> seats) {
        return new Reservation(id, userId, sessionId, createdAt, expiresAt, status, totalPrice, seats);
    }

    public void addSeat(ReservationSeat seat) {
        ensurePending();
        boolean exists = seats.stream().anyMatch(s -> s.getSeatId().equals(seat.getSeatId()));
        if (exists) throw new IllegalStateException("reservation.seat.duplicate");
        seats.add(seat);
        calculateTotal();
    }

    public void removeSeat(Long seatId) {
        ensurePending();
        seats.removeIf(s -> s.getSeatId().equals(seatId));
        calculateTotal();
    }

    public void calculateTotal() {
        Money total = Money.of(java.math.BigDecimal.ZERO);
        for (ReservationSeat s : seats) {
            total = total.add(s.getPrice());
        }
        this.totalPrice = total;
    }

    public void confirm() {
        ensureCanBeConfirmed();
        this.status = ReservationStatus.CONFIRMED;
        registerEvent(new ReservationConfirmedEvent(id, userId, Instant.now()));
    }

    public void cancel(String reason) {
        ensureCanBeCancelled();
        this.status = ReservationStatus.CANCELLED;
        registerEvent(new ReservationCancelledEvent(id, userId, reason, Instant.now()));
    }

    public void ensureCanBeCancelled() {
        if (status != ReservationStatus.PENDING && status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("reservation.cannot.cancel");
        }
    }

    public void ensureCanBeConfirmed() {
        ensurePending();
        if (Instant.now().isAfter(expiresAt)) {
            throw new IllegalStateException("reservation.expired");
        }
        if (seats.isEmpty()) throw new IllegalStateException("reservation.empty");
    }

    public void markExpired() {
        if (status == ReservationStatus.PENDING && Instant.now().isAfter(expiresAt)) {
            status = ReservationStatus.EXPIRED;
        }
    }

    private void ensurePending() {
        if (status != ReservationStatus.PENDING) throw new IllegalStateException("reservation.not.pending");
    }

    @Override
    public UUID id() { return id; }
    public UUID getUserId() { return userId; }
    public UUID getSessionId() { return sessionId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public ReservationStatus getStatus() { return status; }
    public Money getTotalPrice() { return totalPrice; }
    public List<ReservationSeat> getSeats() { return List.copyOf(seats); }
}
