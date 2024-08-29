package io.github.gabrmsouza.subscription.infrastructure.rest.controllers;

import io.github.gabrmsouza.subscription.application.subscription.CancelSubscription;
import io.github.gabrmsouza.subscription.application.subscription.ChargeSubscription;
import io.github.gabrmsouza.subscription.application.subscription.CreateSubscription;
import io.github.gabrmsouza.subscription.infrastructure.authentication.principal.CodeflixUser;
import io.github.gabrmsouza.subscription.infrastructure.rest.SubscriptionRestApi;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.ChargeSubscriptionRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.CreateSubscriptionRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.CancelSubscriptionResponse;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.ChargeSubscriptionResponse;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.CreateSubscriptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class SubscriptionRestController implements SubscriptionRestApi {
    private final CreateSubscription createSubscription;
    private final CancelSubscription cancelSubscription;
    private final ChargeSubscription chargeSubscription;

    public SubscriptionRestController(
            final CreateSubscription createSubscription,
            final CancelSubscription cancelSubscription,
            final ChargeSubscription chargeSubscription
    ) {
        this.createSubscription = Objects.requireNonNull(createSubscription);
        this.cancelSubscription = Objects.requireNonNull(cancelSubscription);
        this.chargeSubscription = Objects.requireNonNull(chargeSubscription);
    }

    @Override
    public ResponseEntity<CreateSubscriptionResponse> createSubscription(final CreateSubscriptionRequest req, final CodeflixUser principal) {
        record CreateSubscriptionInput(Long planId, String accountId) implements CreateSubscription.Input {}
        final var res = this.createSubscription.execute(new CreateSubscriptionInput(req.planId(), principal.accountId()), CreateSubscriptionResponse::new);
        return ResponseEntity.created(URI.create("/subscriptions/" + res.subscriptionId())).body(res);
    }

    @Override
    public ResponseEntity<CancelSubscriptionResponse> cancelSubscription(final CodeflixUser principal) {
        record CancelSubscriptionInput(String accountId) implements CancelSubscription.Input {}
        final var res = this.cancelSubscription.execute(new CancelSubscriptionInput(principal.accountId()), CancelSubscriptionResponse::new);
        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ChargeSubscriptionResponse> chargeActiveSubscription(final ChargeSubscriptionRequest req, final CodeflixUser principal) {
        record ChargeSubscriptionInput(String accountId, String paymentType, String creditCardToken)
                implements ChargeSubscription.Input {}

        final var res = this.chargeSubscription.execute(new ChargeSubscriptionInput(principal.accountId(), req.paymentType(), req.creditCardToken()), ChargeSubscriptionResponse::new);
        return ResponseEntity.ok(res);
    }
}