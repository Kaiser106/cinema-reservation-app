package com.cinema.presentation.request.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseProductRequest(@NotNull Long productId, @Positive int quantity) {}
