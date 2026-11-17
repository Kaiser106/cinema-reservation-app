package com.cinema.presentation.api;

import com.cinema.application.payment.ProcessPaymentCommandHandler;
import com.cinema.application.payment.commands.ProcessPaymentCommand;
import com.cinema.infrastructure.auth.SessionPrincipal;
import com.cinema.presentation.interceptor.IdempotencyInterceptor;
import com.cinema.presentation.request.payment.ProcessPaymentRequest;
import com.cinema.presentation.response.ApiResponse;
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
