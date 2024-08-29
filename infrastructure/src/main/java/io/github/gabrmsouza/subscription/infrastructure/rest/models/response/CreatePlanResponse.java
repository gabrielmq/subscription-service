package io.github.gabrmsouza.subscription.infrastructure.rest.models.response;

import io.github.gabrmsouza.subscription.application.plan.CreatePlan;
import io.github.gabrmsouza.subscription.domain.AssertionConcern;

public record CreatePlanResponse(Long planId) implements AssertionConcern {
    public CreatePlanResponse {
        this.assertArgumentNotNull(planId, "CreatePlanResponse 'planId' should not be null");
    }

    public CreatePlanResponse(CreatePlan.Output out) {
        this(out.planId().value());
    }
}
