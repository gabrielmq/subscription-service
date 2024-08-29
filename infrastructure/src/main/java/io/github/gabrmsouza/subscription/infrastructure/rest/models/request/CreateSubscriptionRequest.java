package io.github.gabrmsouza.subscription.infrastructure.rest.models.request;

import jakarta.validation.constraints.NotNull;

public record CreateSubscriptionRequest(@NotNull Long planId) {
}