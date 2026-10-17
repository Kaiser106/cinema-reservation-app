package com.cinema.domain.payment;

import com.cinema.domain.common.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    @Test
    void shouldCompletePayment() {
        Payment payment = new Payment(UUID.randomUUID(), UUID.randomUUID(), Money.of(new BigDecimal("100.00")), "CARD", "idem-1");
        payment.markCompleted("tx-1");
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals("tx-1", payment.getTransactionRef());
    }

    @Test
    void shouldFailPayment() {
        Payment payment = new Payment(UUID.randomUUID(), UUID.randomUUID(), Money.of(new BigDecimal("100.00")), "CARD", "idem-2");
        payment.markFailed("declined");
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }
}
