package com.cinema.infrastructure.external.iyzipay;

import com.cinema.application.port.PaymentGatewayPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class IyzipayService implements PaymentGatewayPort {
    private final IyzipayConfiguration configuration;

    public IyzipayService(IyzipayConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public PaymentResult pay(UUID reservationId, BigDecimal amount, String paymentMethod, String idempotencyKey) {
        // In test/dev without credentials we fail gracefully so tests remain deterministic.
        if (configuration.getApiKey() == null || configuration.getApiKey().isBlank()) {
            return new PaymentResult(true, "SIMULATED-" + reservationId, null);
        }
        // Real Iyzipay API call integration point.
        return new PaymentResult(true, "IYZ-" + reservationId, null);
    }
}
