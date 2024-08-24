package io.github.gabrmsouza.subscription.domain.subscription;

import io.github.gabrmsouza.subscription.domain.DomainEvent;

public sealed interface SubscriptionEvent
        extends DomainEvent
        permits SubscriptionCreated, SubscriptionCanceled, SubscriptionRenewed, SubscriptionIncomplete {
    String TYPE = "Subscription";

    String subscriptionId();

    @Override
    default String aggregateId() {
        return subscriptionId();
    }

    @Override
    default String aggregateType() {
        return TYPE;
    }
}
