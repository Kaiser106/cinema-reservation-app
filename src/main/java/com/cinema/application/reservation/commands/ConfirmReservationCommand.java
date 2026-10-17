package com.cinema.application.reservation.commands;

import com.cinema.application.common.Command;

import java.util.UUID;

public record ConfirmReservationCommand(UUID reservationId, UUID userId) implements Command<Void> {}
