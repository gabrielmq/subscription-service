package io.github.gabrmsouza.subscription.application.account;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;

public abstract class CreateIdpUser extends UseCase<CreateIdpUser.Input, CreateIdpUser.Output> {
    public interface Input {
        String accountId();
        String firstname();
        String lastname();
        String email();
        String password();
    }

    public interface Output {
        UserId idpUserId();
    }
}
