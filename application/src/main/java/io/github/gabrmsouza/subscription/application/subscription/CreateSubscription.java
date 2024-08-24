package io.github.gabrmsouza.subscription.application.subscription;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;

public abstract class CreateSubscription extends UseCase<CreateSubscription.Input, CreateSubscription.Output> {
    public interface Input {
        String accountId();
        Long planId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }
}
