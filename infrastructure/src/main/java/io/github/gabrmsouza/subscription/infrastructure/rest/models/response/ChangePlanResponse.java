package io.github.gabrmsouza.subscription.infrastructure.rest.models.response;

import io.github.gabrmsouza.subscription.application.plan.ChangePlan;
import io.github.gabrmsouza.subscription.domain.AssertionConcern;

public record ChangePlanResponse(Long planId) implements AssertionConcern {
    public ChangePlanResponse {
        this.assertArgumentNotNull(planId, "ChangePlanResponse 'planId' should not be null");
    }

    public ChangePlanResponse(ChangePlan.Output out) {
        this(out.planId().value());
    }
}