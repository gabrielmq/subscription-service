package io.github.gabrmsouza.subscription.application.account;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.account.AccountId;

public abstract class UpdateBillingInfo extends UseCase<UpdateBillingInfo.Input, UpdateBillingInfo.Output> {
    public interface Input {
        String accountId();
        String zipcode();
        String number();
        String complement();
        String country();
    }

    public interface Output {
        AccountId accountId();
    }
}