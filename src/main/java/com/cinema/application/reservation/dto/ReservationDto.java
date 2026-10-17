package com.cinema.application.reservation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReservationDto(UUID id, UUID sessionId, String status, BigDecimal totalPrice, Instant createdAt, Instant expiresAt, List<Long> seatIds) {}
