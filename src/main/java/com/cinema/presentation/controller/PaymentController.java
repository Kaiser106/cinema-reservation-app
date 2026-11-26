package com.cinema.presentation.controller;

import com.cinema.application.features.payment.commands.process.ProcessPaymentCommand;
import com.cinema.application.features.payment.commands.process.ProcessPaymentCommandHandler;
import com.cinema.infrastructure.auth.SessionPrincipal;
import com.cinema.presentation.dto.request.payment.ProcessPaymentRequest;
import com.cinema.presentation.dto.response.ApiResponse;
import com.cinema.presentation.interceptor.IdempotencyInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final ProcessPaymentCommandHandler processPaymentCommandHandler;

    public PaymentController(ProcessPaymentCommandHandler processPaymentCommandHandler) {
        this.processPaymentCommandHandler = processPaymentCommandHandler;
    }

    @PostMapping
    public ApiResponse<Map<String, UUID>> pay(@Valid @RequestBody ProcessPaymentRequest request,
                                               @RequestAttribute(IdempotencyInterceptor.ATTRIBUTE) String idempotencyKey,
                                               Authentication auth,
                                               HttpServletRequest req) {
        SessionPrincipal p = (SessionPrincipal) auth.getPrincipal();
        UUID paymentId = processPaymentCommandHandler.handle(new ProcessPaymentCommand(request.reservationId(), p.userId(), request.amount(), request.paymentMethod(), idempotencyKey));
        return ApiResponse.ok(Map.of("paymentId", paymentId), req.getRequestURI());
    }
}
