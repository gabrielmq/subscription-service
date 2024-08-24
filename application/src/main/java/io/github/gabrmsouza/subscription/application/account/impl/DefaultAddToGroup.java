package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.account.AddToGroup;
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

public class DefaultAddToGroup extends AddToGroup {
    private final AccountGateway accountGateway;
    private final IdentityProviderGateway identityProviderGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultAddToGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public Output execute(final AddToGroup.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultAddToGroup cannot be null");
        }

        final var anAccountId = new AccountId(in.accountId());
        final var aSubscriptionId = new SubscriptionId(in.subscriptionId());

        final var aSubscription = subscriptionGateway.subscriptionOfId(aSubscriptionId)
                .filter(it -> it.accountId().equals(anAccountId))
                .orElseThrow(() -> DomainException.notFound(Subscription.class, aSubscriptionId));

        if (aSubscription.isTrail() || aSubscription.isActive()) {
            final var userId = this.accountGateway.accountOfId(anAccountId)
                    .orElseThrow(() -> DomainException.notFound(Account.class, anAccountId))
                    .userId();
            this.identityProviderGateway.addUserToGroup(userId, new GroupId(in.groupId()));
        }
        return new StdOutput(aSubscriptionId);
    }

    record StdOutput(SubscriptionId subscriptionId) implements Output {
    }
}
