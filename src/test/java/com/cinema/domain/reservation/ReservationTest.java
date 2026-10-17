package com.cinema.domain.reservation;

import com.cinema.domain.common.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    @Test
    void shouldCalculateTotalAndConfirm() {
        Reservation reservation = new Reservation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(600));
        reservation.addSeat(new ReservationSeat(UUID.randomUUID(), 1L, reservation.getSessionId(), Money.of(new BigDecimal("120.00"))));
        reservation.addSeat(new ReservationSeat(UUID.randomUUID(), 2L, reservation.getSessionId(), Money.of(new BigDecimal("120.00"))));
        assertEquals(new BigDecimal("240.00"), reservation.getTotalPrice().toBigDecimal());

        reservation.confirm();
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    }

    @Test
    void shouldCancelPendingReservation() {
        Reservation reservation = new Reservation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), Instant.now(), Instant.now().plusSeconds(600));
        reservation.cancel("test");
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }
}
