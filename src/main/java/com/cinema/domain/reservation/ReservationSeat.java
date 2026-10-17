package com.cinema.domain.reservation;

import com.cinema.domain.common.Money;

import java.util.UUID;

public class ReservationSeat {
    private final UUID id;
    private final Long seatId;
    private final UUID sessionId;
    private final Money price;

    public ReservationSeat(UUID id, Long seatId, UUID sessionId, Money price) {
        this.id = id;
        this.seatId = seatId;
        this.sessionId = sessionId;
        this.price = price;
    }

    public UUID getId() { return id; }
    public Long getSeatId() { return seatId; }
    public UUID getSessionId() { return sessionId; }
    public Money getPrice() { return price; }
}
