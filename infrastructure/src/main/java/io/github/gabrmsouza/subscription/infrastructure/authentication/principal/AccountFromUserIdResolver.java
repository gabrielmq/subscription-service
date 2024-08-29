package io.github.gabrmsouza.subscription.infrastructure.authentication.principal;

import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;

import java.util.Optional;
import java.util.function.Function;

public interface AccountFromUserIdResolver extends Function<UserId, Optional<Account>> {
}