package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.account.RemoveFromGroup;
import io.github.gabrmsouza.subscription.domain.AggregateRoot;
import io.github.gabrmsouza.subscription.domain.Identifier;
import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.GroupId;
import io.github.gabrmsouza.subscription.domain.account.idp.IdentityProviderGateway;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;

import java.util.Objects;

public class DefaultRemoveFromGroup extends RemoveFromGroup {
    private final AccountGateway accountGateway;
    private final IdentityProviderGateway identityProviderGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultRemoveFromGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final RemoveFromGroup.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultRemoveFromGroup cannot be null");
        }

        final var anAccountId = new AccountId(in.accountId());
        final var aSubscriptionId = new SubscriptionId(in.subscriptionId());

        final var aSubscription = subscriptionGateway.subscriptionOfId(aSubscriptionId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow(() -> notFound(Subscription.class, aSubscriptionId));

        if (isRemovableStatus(aSubscription) && aSubscription.isExpired()) {
            final var userId = this.accountGateway.accountOfId(anAccountId)
                    .orElseThrow(() -> notFound(Account.class, anAccountId))
                    .userId();

            this.identityProviderGateway.removeUserFromGroup(userId, new GroupId(in.groupId()));
        }

        return new StdOutput(aSubscriptionId);
    }

    private static boolean isRemovableStatus(final Subscription aSubscription) {
        return aSubscription.isCanceled() || aSubscription.isIncomplete();
    }

    private RuntimeException notFound(Class<? extends AggregateRoot<?>> aggClass, Identifier id) {
        return DomainException.with("%s with id %s was not found".formatted(aggClass.getCanonicalName(), id.value()));
    }

    record StdOutput(SubscriptionId subscriptionId) implements Output {
    }
}
