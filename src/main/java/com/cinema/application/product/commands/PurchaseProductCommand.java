package com.cinema.application.product.commands;

import com.cinema.application.common.Command;

import java.util.UUID;

public record PurchaseProductCommand(UUID userId, Long productId, int quantity) implements Command<Void> {}
