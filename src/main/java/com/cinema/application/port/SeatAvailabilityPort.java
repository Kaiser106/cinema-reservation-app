package com.cinema.application.port;

import java.util.List;
import java.util.UUID;

public interface SeatAvailabilityPort {
    void lockSessionAndSeats(UUID sessionId, List<Long> seatIds);
    boolean hasReservedSeat(UUID sessionId, List<Long> seatIds);
    List<Long> findAvailableSeatIds(UUID sessionId);
}
