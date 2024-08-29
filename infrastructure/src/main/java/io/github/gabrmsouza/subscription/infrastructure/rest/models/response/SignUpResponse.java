package io.github.gabrmsouza.subscription.infrastructure.rest.models.response;

import io.github.gabrmsouza.subscription.application.account.CreateAccount;
import io.github.gabrmsouza.subscription.domain.AssertionConcern;

public record SignUpResponse(String accountId) implements AssertionConcern {
    public SignUpResponse {
        this.assertArgumentNotEmpty(accountId, "'accountId' must not be empty");
    }

    public SignUpResponse(final CreateAccount.Output out) {
        this(out.accountId().value());
    }
}
