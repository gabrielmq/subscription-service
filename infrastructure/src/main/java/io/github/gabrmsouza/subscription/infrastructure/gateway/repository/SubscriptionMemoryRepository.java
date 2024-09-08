package io.github.gabrmsouza.subscription.infrastructure.gateway.repository;

import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import io.github.gabrmsouza.subscription.domain.utils.IDUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("dev")
public class SubscriptionMemoryRepository implements SubscriptionGateway {

    private final Map<String, Subscription> db = new ConcurrentHashMap<>();
    private final Map<String, Set<Subscription>> accountIndex = new ConcurrentHashMap<>();

    @Override
    public Optional<Subscription> latestSubscriptionOfAccount(AccountId accountId) {
        return accountIndex.getOrDefault(accountId.value(), Set.of()).stream().findFirst();
    }

    @Override
    public Optional<Subscription> subscriptionOfId(SubscriptionId subscriptionId) {
        return Optional.ofNullable(this.db.get(subscriptionId.value()));
    }

    @Override
    public Subscription save(Subscription subscription) {
        this.db.put(subscription.id().value(), subscription);
        updateAccountIndex(subscription);
        return subscription;
    }

    @Override
    public SubscriptionId nextId() {
        return new SubscriptionId(IDUtils.uuid());
    }

    private void updateAccountIndex(Subscription subscription) {
        var bag = this.accountIndex.getOrDefault(subscription.accountId().value(), new HashSet<>());
        bag.add(subscription);
        this.accountIndex.put(subscription.accountId().value(), bag);
    }
}
