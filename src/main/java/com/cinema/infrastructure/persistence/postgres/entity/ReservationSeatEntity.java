package com.cinema.infrastructure.persistence.postgres.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "reservation_seats")
public class ReservationSeatEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "session_id", nullable = false, columnDefinition = "uuid")
    private UUID sessionId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Version
    private Long version;
    // Optimistic version is complementary for stale-update detection; double-book prevention is pessimistic lock based.

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public ReservationEntity getReservation() { return reservation; }
    public void setReservation(ReservationEntity reservation) { this.reservation = reservation; }
    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }
    public UUID getSessionId() { return sessionId; }
    public void setSessionId(UUID sessionId) { this.sessionId = sessionId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Long getVersion() { return version; }
}
