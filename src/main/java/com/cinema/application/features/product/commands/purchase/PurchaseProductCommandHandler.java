package com.cinema.application.features.product.commands.purchase;

import com.cinema.application.cqrs.CommandHandler;
import com.cinema.application.shared.exception.BusinessException;
import com.cinema.application.shared.exception.ErrorCodes;
import com.cinema.application.shared.ports.OutboxRepository;
import com.cinema.domain.point.events.PointsEarnedEvent;
import com.cinema.domain.point.model.UserPoint;
import com.cinema.domain.point.repository.UserPointRepository;
import com.cinema.domain.product.events.ProductPurchasedEvent;
import com.cinema.domain.product.model.Product;
import com.cinema.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class PurchaseProductCommandHandler implements CommandHandler<PurchaseProductCommand, Void> {
    private final ProductRepository productRepository;
    private final UserPointRepository userPointRepository;
    private final OutboxRepository outboxRepository;

    public PurchaseProductCommandHandler(ProductRepository productRepository, UserPointRepository userPointRepository, OutboxRepository outboxRepository) {
        this.productRepository = productRepository;
        this.userPointRepository = userPointRepository;
        this.outboxRepository = outboxRepository;
    }

    @Override
    @Transactional
    public Void handle(PurchaseProductCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.PRODUCT_NOT_FOUND, "error.product.notFound"));
        product.purchase(command.quantity());
        productRepository.save(product);

        UserPoint userPoint = userPointRepository.findByUserId(command.userId()).orElse(new UserPoint(command.userId(), 0));
        int earned = command.quantity() * 2;
        userPoint.earn(earned);
        userPointRepository.save(userPoint);

        outboxRepository.storeAll(java.util.List.of(
                new ProductPurchasedEvent(command.userId(), command.productId(), command.quantity(), Instant.now()),
                new PointsEarnedEvent(command.userId(), earned, Instant.now())
        ));
        return null;
    }
}
