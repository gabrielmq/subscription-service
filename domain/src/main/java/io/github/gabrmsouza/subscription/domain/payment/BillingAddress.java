package io.github.gabrmsouza.subscription.domain.payment;

import io.github.gabrmsouza.subscription.domain.ValueObject;

public record BillingAddress(String zipcode, String number, String complement, String country) implements ValueObject {

    public BillingAddress {
        this.assertArgumentNotEmpty(zipcode, "'zipcode' should not be empty");
        this.assertArgumentNotEmpty(number, "'number' should not be empty");
        this.assertArgumentNotEmpty(country, "'country' should not be empty");
    }
}