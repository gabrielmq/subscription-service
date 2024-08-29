package io.github.gabrmsouza.subscription.infrastructure.rest.models.response;

import io.github.gabrmsouza.subscription.application.subscription.CreateSubscription;
import io.github.gabrmsouza.subscription.domain.AssertionConcern;

public record CreateSubscriptionResponse(String subscriptionId) implements AssertionConcern {
    public CreateSubscriptionResponse {
        this.assertArgumentNotEmpty(subscriptionId, "CreateSubscriptionResponse 'subscriptionId' should not be empty");
    }

    public CreateSubscriptionResponse(CreateSubscription.Output out) {
        this(out.subscriptionId().value());
    }
}
