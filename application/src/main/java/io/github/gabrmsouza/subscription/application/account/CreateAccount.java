package io.github.gabrmsouza.subscription.application.account;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.account.AccountId;

public abstract class CreateAccount extends UseCase<CreateAccount.Input, CreateAccount.Output> {
    public interface Input {
        String userId();
        String accountId();
        String email();
        String firstname();
        String lastname();
        String documentNumber();
        String documentType();
    }

    public interface Output {
        AccountId accountId();
    }
}