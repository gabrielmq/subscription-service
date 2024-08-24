package io.github.gabrmsouza.subscription.application.subscription;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.payment.Transaction;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;

import java.time.LocalDate;

public abstract class ChargeSubscription extends UseCase<ChargeSubscription.Input, ChargeSubscription.Output> {
    public interface Input {
        String accountId();
        String paymentType();
        String creditCardToken();
    }

    public interface Output {
        SubscriptionId subscriptionId();
        String subscriptionStatus();
        LocalDate subscriptionDueDate();
        Transaction paymentTransaction();
    }
}