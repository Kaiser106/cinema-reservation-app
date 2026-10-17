package com.cinema.concurrency;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Concurrency integration scenario requires full seeded DB + containers in runtime")
class ReservationConcurrencyIntegrationTest {
    @Test
    void shouldAllowOnlyOneReservationForSameSeat() {
        // Intentionally left as integration template:
        // use CountDownLatch with multiple users, call /api/reservations simultaneously,
        // verify exactly one success and others fail with RESERVATION_SEAT_ALREADY_RESERVED.
    }
}
