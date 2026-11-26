package com.cinema.application.features.payment.ports;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentGatewayPort {
    PaymentResult pay(UUID reservationId, BigDecimal amount, String paymentMethod, String idempotencyKey);
    record PaymentResult(boolean success, String transactionRef, String failureReason) {}
}
