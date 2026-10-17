package com.cinema.application.reservation.commands;

import com.cinema.application.common.Command;

import java.util.List;
import java.util.UUID;

public record CreateReservationCommand(UUID userId, UUID sessionId, List<Long> seatIds) implements Command<UUID> {}
