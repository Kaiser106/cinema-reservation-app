package com.cinema.presentation.request.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentRequest(@NotNull UUID reservationId, @NotNull @Positive BigDecimal amount, @NotBlank String paymentMethod) {}
