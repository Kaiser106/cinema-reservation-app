package com.cinema.presentation.request.reservation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateReservationRequest(@NotNull UUID sessionId, @NotEmpty List<Long> seatIds) {}
