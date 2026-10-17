package com.cinema.application.payment.commands;

import com.cinema.application.common.Command;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(UUID reservationId, UUID userId, BigDecimal amount, String paymentMethod, String idempotencyKey) implements Command<UUID> {}
