package io.github.gabrmsouza.subscription.domain.account;

import io.github.gabrmsouza.subscription.domain.account.idp.UserId;

import java.util.Optional;

public interface AccountGateway {
    AccountId nextId();
    Optional<Account> accountOfId(AccountId anId);
    Optional<Account> accountOfUserId(UserId userId);
    Account save(Account anAccount);
}
