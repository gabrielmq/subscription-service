package io.github.gabrmsouza.subscription.application.subscription.impl;

import io.github.gabrmsouza.subscription.application.subscription.ChargeSubscription;
import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.domain.payment.BillingAddress;
import io.github.gabrmsouza.subscription.domain.payment.Payment;
import io.github.gabrmsouza.subscription.domain.payment.PaymentGateway;
import io.github.gabrmsouza.subscription.domain.payment.Transaction;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionCommand;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import io.github.gabrmsouza.subscription.domain.utils.IDUtils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DefaultChargeSubscription extends ChargeSubscription {
    private static final int MAX_INCOMPLETE_DAYS = 2;

    private final AccountGateway accountGateway;
    private final Clock clock;
    private final PaymentGateway paymentGateway;
    private final PlanGateway planGateway;
    private final SubscriptionGateway subscriptionGateway;

    public DefaultChargeSubscription(
            final AccountGateway accountGateway,
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        this.accountGateway = Objects.requireNonNull(accountGateway);
        this.clock = Objects.requireNonNull(clock);
        this.paymentGateway = Objects.requireNonNull(paymentGateway);
        this.planGateway = Objects.requireNonNull(planGateway);
        this.subscriptionGateway = Objects.requireNonNull(subscriptionGateway);
    }

    @Override
    public ChargeSubscription.Output execute(final ChargeSubscription.Input in) {
        if (in == null) {
            throw new IllegalArgumentException("Input of DefaultChargeSubscription cannot be null");
        }

        final var accountId = new AccountId(in.accountId());
        final var now = clock.instant();

        final var aSubscription = subscriptionGateway.latestSubscriptionOfAccount(accountId)
                .filter(it -> it.accountId().equals(accountId))
                .orElseThrow(() -> DomainException.with("Subscription for account %s was not found".formatted(in.accountId())));

        if (aSubscription.dueDate().isAfter(LocalDate.ofInstant(now, ZoneId.systemDefault()))) {
            return new StdOutput(aSubscription.id(), aSubscription.status().value(), aSubscription.dueDate(), null);
        }

        final var aPlan = this.planGateway.planOfId(aSubscription.planId())
                .orElseThrow(() -> DomainException.notFound(Plan.class, aSubscription.planId()));

        final var anUserAccount = this.accountGateway.accountOfId(accountId)
                .orElseThrow(() -> DomainException.notFound(Account.class, accountId));

        final var aPayment = this.newPaymentWith(in, aPlan, anUserAccount);
        final var actualTransaction = this.paymentGateway.processPayment(aPayment);

        if (actualTransaction.isSuccess()) {
            aSubscription.execute(new SubscriptionCommand.RenewSubscription(aPlan, actualTransaction.transactionId()));
        } else if (hasTolerableDays(aSubscription.dueDate(), now)) {
            aSubscription.execute(new SubscriptionCommand.IncompleteSubscription(actualTransaction.errorMessage(), actualTransaction.transactionId()));
        } else {
            aSubscription.execute(new SubscriptionCommand.CancelSubscription());
        }

        this.subscriptionGateway.save(aSubscription);
        return new StdOutput(aSubscription.id(), aSubscription.status().value(), aSubscription.dueDate(), actualTransaction);
    }

    private boolean hasTolerableDays(final LocalDate dueDate, final Instant now) {
        return ChronoUnit.DAYS.between(dueDate, LocalDate.ofInstant(now, ZoneOffset.UTC)) <= MAX_INCOMPLETE_DAYS;
    }

    private Payment newPaymentWith(final Input in, final Plan aPlan, final Account anUserAccount) {
        return Payment.create(
                in.paymentType(),
                IDUtils.uuid(),
                aPlan.price().amount(),
                new BillingAddress(
                        anUserAccount.billingAddress().zipcode(),
                        anUserAccount.billingAddress().number(),
                        anUserAccount.billingAddress().complement(),
                        anUserAccount.billingAddress().country()
                ),
                in.creditCardToken()
        );
    }

    record StdOutput(
            SubscriptionId subscriptionId,
            String subscriptionStatus,
            LocalDate subscriptionDueDate,
            Transaction paymentTransaction
    ) implements ChargeSubscription.Output {}
}
