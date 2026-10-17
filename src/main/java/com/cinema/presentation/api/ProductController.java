package com.cinema.presentation.api;

import com.cinema.application.product.PurchaseProductCommandHandler;
import com.cinema.application.product.commands.PurchaseProductCommand;
import com.cinema.application.port.ProductPort;
import com.cinema.infrastructure.auth.SessionPrincipal;
import com.cinema.presentation.request.product.PurchaseProductRequest;
import com.cinema.presentation.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductPort productPort;
    private final PurchaseProductCommandHandler purchaseHandler;

    public ProductController(ProductPort productPort, PurchaseProductCommandHandler purchaseHandler) {
        this.productPort = productPort;
        this.purchaseHandler = purchaseHandler;
    }

    @GetMapping
    public ApiResponse<?> list(HttpServletRequest req) {
        return ApiResponse.ok(productPort.findAll().stream().map(p -> Map.of(
                "id", p.getId(),
                "name", p.getName(),
                "price", p.getPrice().toBigDecimal(),
                "stock", p.getStock()
        )).toList(), req.getRequestURI());
    }

    @PostMapping("/purchase")
    public ApiResponse<Void> purchase(@Valid @RequestBody PurchaseProductRequest request, Authentication auth, HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        purchaseHandler.handle(new PurchaseProductCommand(p.userId(), request.productId(), request.quantity()));
        return ApiResponse.ok(null, req.getRequestURI());
    }
}
