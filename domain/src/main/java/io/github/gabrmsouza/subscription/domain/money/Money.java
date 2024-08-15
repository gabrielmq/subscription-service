package io.github.gabrmsouza.subscription.domain.money;

import io.github.gabrmsouza.subscription.domain.ValueObject;

import java.util.Currency;

public record Money(Currency currency, Double amount) implements ValueObject {
    public Money {
        this.assertArgumentNotNull(currency, "'currency' should not be null");
        this.assertArgumentNotNull(amount, "'amount' should not be null");
    }

    public Money(final String currency, final Double amount) {
        this(Currency.getInstance(currency), amount);
    }
}
