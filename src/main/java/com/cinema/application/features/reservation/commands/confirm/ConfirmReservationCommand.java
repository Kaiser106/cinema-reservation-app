package com.cinema.application.features.reservation.commands.confirm;

import com.cinema.application.cqrs.Command;

import java.util.UUID;

public record ConfirmReservationCommand(UUID reservationId, UUID userId) implements Command<Void> {}
