package io.github.gabrmsouza.subscription.infrastructure.rest.models.response;

import io.github.gabrmsouza.subscription.application.subscription.ChargeSubscription;

public record ChargeSubscriptionResponse(
        String subscriptionId,
        String subscriptionStatus,
        String subscriptionDueDate,
        String paymentTransactionId,
        String paymentTransactionError
) {

    public ChargeSubscriptionResponse(ChargeSubscription.Output out) {
        this(
                out.subscriptionId().value(),
                out.subscriptionStatus(),
                out.subscriptionDueDate().toString(),
                out.paymentTransaction() == null ? null : out.paymentTransaction().transactionId(),
                out.paymentTransaction() == null ? null : out.paymentTransaction().errorMessage()
        );
    }
}