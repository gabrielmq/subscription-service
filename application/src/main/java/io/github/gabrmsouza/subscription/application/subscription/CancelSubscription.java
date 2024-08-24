package io.github.gabrmsouza.subscription.application.subscription;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;

public abstract class CancelSubscription extends UseCase<CancelSubscription.Input, CancelSubscription.Output> {
    public interface Input {
        String accountId();
    }

    public interface Output {
        String subscriptionStatus();
        SubscriptionId subscriptionId();
    }
}