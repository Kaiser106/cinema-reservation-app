package com.cinema.application.features.reservation.queries;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReservationDto(UUID id, UUID sessionId, String status, BigDecimal totalPrice, Instant createdAt, Instant expiresAt, List<Long> seatIds) {}
