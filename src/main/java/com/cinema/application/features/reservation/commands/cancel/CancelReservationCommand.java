package com.cinema.application.features.reservation.commands.cancel;

import com.cinema.application.cqrs.Command;

import java.util.UUID;

public record CancelReservationCommand(UUID reservationId, UUID userId, String reason) implements Command<Void> {}
