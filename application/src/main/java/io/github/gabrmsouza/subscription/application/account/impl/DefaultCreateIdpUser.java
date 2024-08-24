package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.account.CreateIdpUser;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.IdentityProviderGateway;
import io.github.gabrmsouza.subscription.domain.account.idp.User;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import io.github.gabrmsouza.subscription.domain.person.Email;
import io.github.gabrmsouza.subscription.domain.person.Name;

import java.util.Objects;

public class DefaultCreateIdpUser extends CreateIdpUser {
    private final IdentityProviderGateway identityProviderGateway;

    public DefaultCreateIdpUser(final IdentityProviderGateway identityProviderGateway) {
        this.identityProviderGateway = Objects.requireNonNull(identityProviderGateway);
    }

    @Override
    public Output execute(final Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input to DefaultCreateIdpUser cannot be null");
        }
        final var user = this.userWith(in);
        return new StdOutput(this.identityProviderGateway.create(user));
    }

    private User userWith(final Input in) {
        return User.newUser(
                new AccountId(in.accountId()),
                new Name(in.firstname(), in.lastname()),
                new Email(in.email()),
                in.password()
        );
    }

    record StdOutput(UserId idpUserId) implements Output {
    }
}
