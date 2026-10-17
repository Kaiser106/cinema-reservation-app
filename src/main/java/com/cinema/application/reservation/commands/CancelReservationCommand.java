package com.cinema.application.reservation.commands;

import com.cinema.application.common.Command;

import java.util.UUID;

public record CancelReservationCommand(UUID reservationId, UUID userId, String reason) implements Command<Void> {}
