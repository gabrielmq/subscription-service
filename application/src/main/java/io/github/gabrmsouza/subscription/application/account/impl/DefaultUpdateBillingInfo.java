package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.account.UpdateBillingInfo;
import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.AccountCommand;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.domain.person.Address;

import java.util.Objects;

public class DefaultUpdateBillingInfo extends UpdateBillingInfo {
    private final AccountGateway accountGateway;

    public DefaultUpdateBillingInfo(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw DomainException.with("Invalid input");
        }

        final var anAccountId = new AccountId(input.accountId());

        final var anAccount = this.accountGateway.accountOfId(anAccountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, anAccountId));

        anAccount.execute(new AccountCommand.ChangeProfile(anAccount.name(), newBillingAddress(input)));
        this.accountGateway.save(anAccount);
        return new StdOutput(anAccountId);
    }

    private Address newBillingAddress(final Input input) {
        return new Address(input.zipcode(), input.number(), input.complement(), input.country());
    }

    record StdOutput(AccountId accountId) implements Output {}
}
