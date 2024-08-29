package io.github.gabrmsouza.subscription.infrastructure.rest.models.request;

import jakarta.validation.constraints.NotBlank;

public record ChargeSubscriptionRequest(
        @NotBlank String paymentType,
        String creditCardToken
) {
}
