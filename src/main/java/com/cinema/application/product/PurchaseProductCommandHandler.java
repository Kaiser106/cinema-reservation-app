package com.cinema.application.product;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.domain.repository.OutboxRepository;
import com.cinema.domain.repository.ProductRepository;
import com.cinema.domain.repository.UserPointRepository;
import com.cinema.application.product.commands.PurchaseProductCommand;
import com.cinema.domain.point.UserPoint;
import com.cinema.domain.point.events.PointsEarnedEvent;
import com.cinema.domain.product.Product;
import com.cinema.domain.product.events.ProductPurchasedEvent;
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
