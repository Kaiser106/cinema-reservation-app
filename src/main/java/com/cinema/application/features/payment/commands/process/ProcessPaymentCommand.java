package com.cinema.application.features.payment.commands.process;

import com.cinema.application.cqrs.Command;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(UUID reservationId, UUID userId, BigDecimal amount, String paymentMethod, String idempotencyKey) implements Command<UUID> {}
