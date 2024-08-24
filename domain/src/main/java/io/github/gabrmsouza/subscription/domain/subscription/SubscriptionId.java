package io.github.gabrmsouza.subscription.domain.subscription;

import io.github.gabrmsouza.subscription.domain.Identifier;

public record SubscriptionId(String value) implements Identifier<String> {
    public SubscriptionId {
        this.assertArgumentNotEmpty(value, "'subscriptionId' should not be empty");
    }
}