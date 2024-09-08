package io.github.gabrmsouza.subscription.infrastructure.gateway.repository;

import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import io.github.gabrmsouza.subscription.domain.utils.IDUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("dev")
public class AccountInMemoryRepository implements AccountGateway {

    private Map<String, Account> db = new ConcurrentHashMap<>();
    private Map<String, Account> userIdIndex = new ConcurrentHashMap<>();

    @Override
    public AccountId nextId() {
        return new AccountId(IDUtils.uuid());
    }

    @Override
    public Optional<Account> accountOfId(AccountId anId) {
        return Optional.ofNullable(this.db.get(anId.value()));
    }

    @Override
    public Optional<Account> accountOfUserId(UserId userId) {
        return Optional.ofNullable(this.userIdIndex.get(userId.value()));
    }

    @Override
    public Account save(Account anAccount) {
        this.db.put(anAccount.id().value(), anAccount);
        this.userIdIndex.put(anAccount.userId().value(), anAccount);
        return anAccount;
    }
}
