package com.cinema.application.features.reservation.commands.create;

import com.cinema.application.cqrs.Command;

import java.util.List;
import java.util.UUID;

public record CreateReservationCommand(UUID userId, UUID sessionId, List<Long> seatIds) implements Command<UUID> {}
