package com.cinema.application.product;

import com.cinema.application.common.BusinessException;
import com.cinema.application.common.CommandHandler;
import com.cinema.application.common.ErrorCodes;
import com.cinema.application.port.OutboxPort;
import com.cinema.application.port.ProductPort;
import com.cinema.application.port.UserPointPort;
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
    private final ProductPort productPort;
    private final UserPointPort userPointPort;
    private final OutboxPort outboxPort;

    public PurchaseProductCommandHandler(ProductPort productPort, UserPointPort userPointPort, OutboxPort outboxPort) {
        this.productPort = productPort;
        this.userPointPort = userPointPort;
        this.outboxPort = outboxPort;
    }

    @Override
    @Transactional
    public Void handle(PurchaseProductCommand command) {
        Product product = productPort.findById(command.productId())
                .orElseThrow(() -> new BusinessException(ErrorCodes.PRODUCT_NOT_FOUND, "error.product.notFound"));
        product.purchase(command.quantity());
        productPort.save(product);

        UserPoint userPoint = userPointPort.findByUserId(command.userId()).orElse(new UserPoint(command.userId(), 0));
        int earned = command.quantity() * 2;
        userPoint.earn(earned);
        userPointPort.save(userPoint);

        outboxPort.storeAll(java.util.List.of(
                new ProductPurchasedEvent(command.userId(), command.productId(), command.quantity(), Instant.now()),
                new PointsEarnedEvent(command.userId(), earned, Instant.now())
        ));
        return null;
    }
}
