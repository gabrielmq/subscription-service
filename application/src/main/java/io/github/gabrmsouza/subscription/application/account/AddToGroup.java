package io.github.gabrmsouza.subscription.application.account;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;

public abstract class AddToGroup extends UseCase<AddToGroup.Input, AddToGroup.Output> {
    public interface Input {
        String accountId();
        String subscriptionId();
        String groupId();
    }

    public interface Output {
        SubscriptionId subscriptionId();
    }
}