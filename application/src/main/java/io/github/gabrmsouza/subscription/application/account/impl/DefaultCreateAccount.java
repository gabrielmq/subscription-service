package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.account.CreateAccount;
import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import io.github.gabrmsouza.subscription.domain.person.Document;
import io.github.gabrmsouza.subscription.domain.person.Email;
import io.github.gabrmsouza.subscription.domain.person.Name;

import java.util.Objects;

public class DefaultCreateAccount extends CreateAccount {
    private final AccountGateway accountGateway;

    public DefaultCreateAccount(final AccountGateway accountGateway) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
    }

    @Override
    public Output execute(final Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultCreateAccount cannot be null");
        }

        final var anUserAccount = this.newAccountWith(in);
        this.accountGateway.save(anUserAccount);
        return new StdOutput(anUserAccount.id());
    }

    private Account newAccountWith(final Input in) {
        return Account.newAccount(
                new AccountId(in.accountId()),
                new UserId(in.userId()),
                new Email(in.email()),
                new Name(in.firstname(), in.lastname()),
                Document.create(in.documentNumber(), in.documentType())
        );
    }

    record StdOutput(AccountId accountId) implements Output {
    }
}
