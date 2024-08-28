package io.github.gabrmsouza.subscription.infrastructure.mediator;


import io.github.gabrmsouza.subscription.application.account.CreateAccount;
import io.github.gabrmsouza.subscription.application.account.CreateIdpUser;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.SignUpRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.SignUpResponse;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class SignUpMediator {
    private final AccountGateway accountGateway;
    private final CreateAccount createAccount;
    private final CreateIdpUser createIdpUser;

    public SignUpMediator(
            final AccountGateway accountGateway,
            final CreateAccount createAccount,
            final CreateIdpUser createIdpUser
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.createAccount = Objects.requireNonNull(createAccount);
        this.createIdpUser = Objects.requireNonNull(createIdpUser);
    }

    public SignUpResponse signUp(final SignUpRequest req) {
        return nextAccountId()
                .andThen(createIdpUser())
                .andThen(createAccount())
                .apply(req);
    }

    private Function<SignUpRequest, SignUpRequest> nextAccountId() {
        return req -> req.with(this.accountGateway.nextId());
    }

    private Function<SignUpRequest, SignUpRequest> createIdpUser() {
        return req -> this.createIdpUser.execute(req, req::with);
    }

    private Function<SignUpRequest, SignUpResponse> createAccount() {
        return req -> this.createAccount.execute(req, SignUpResponse::new);
    }
}
