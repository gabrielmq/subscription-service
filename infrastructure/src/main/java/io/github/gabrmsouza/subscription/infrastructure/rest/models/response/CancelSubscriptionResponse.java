package io.github.gabrmsouza.subscription.infrastructure.rest.models.response;

import io.github.gabrmsouza.subscription.application.subscription.CancelSubscription;
import io.github.gabrmsouza.subscription.domain.AssertionConcern;

public record CancelSubscriptionResponse(String subscriptionId, String subscriptionStatus) implements AssertionConcern {
    public CancelSubscriptionResponse {
        this.assertArgumentNotEmpty(subscriptionId, "CancelSubscriptionResponse 'subscriptionId' should not be empty");
        this.assertArgumentNotEmpty(subscriptionStatus, "CancelSubscriptionResponse 'subscriptionStatus' should not be empty");
    }

    public CancelSubscriptionResponse(CancelSubscription.Output out) {
        this(out.subscriptionId().value(), out.subscriptionStatus());
    }
}
