package com.cinema.application.session.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SessionDto(UUID id, UUID movieId, Long hallId, Instant startTime, Instant endTime, BigDecimal price) {}
