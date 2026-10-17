package com.cinema.domain.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;

    private Money(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("money.amount.null");
        if (amount.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("money.amount.negative");
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int times) {
        if (times < 0) throw new IllegalArgumentException("money.multiplier.negative");
        return new Money(this.amount.multiply(BigDecimal.valueOf(times)));
    }

    public BigDecimal toBigDecimal() { return amount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() { return Objects.hash(amount); }
}
