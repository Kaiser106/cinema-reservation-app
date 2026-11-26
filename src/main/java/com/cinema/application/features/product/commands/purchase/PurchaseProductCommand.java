package com.cinema.application.features.product.commands.purchase;

import com.cinema.application.cqrs.Command;

import java.util.UUID;

public record PurchaseProductCommand(UUID userId, Long productId, int quantity) implements Command<Void> {}
