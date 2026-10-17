package com.cinema.domain.reservation;

import com.cinema.domain.common.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationSeatTest {
    @Test
    void shouldHoldSeatPrice() {
        ReservationSeat seat = new ReservationSeat(UUID.randomUUID(), 12L, UUID.randomUUID(), Money.of(new BigDecimal("99.90")));
        assertEquals(12L, seat.getSeatId());
        assertEquals(new BigDecimal("99.90"), seat.getPrice().toBigDecimal());
    }
}
