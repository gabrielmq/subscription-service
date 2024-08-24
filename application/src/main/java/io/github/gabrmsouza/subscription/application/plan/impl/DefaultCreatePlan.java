package io.github.gabrmsouza.subscription.application.plan.impl;

import io.github.gabrmsouza.subscription.application.plan.CreatePlan;
import io.github.gabrmsouza.subscription.domain.money.Money;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultCreatePlan extends CreatePlan {
    private final PlanGateway planGateway;

    public DefaultCreatePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public CreatePlan.Output execute(final CreatePlan.Input in) {
        final var aPlan = this.planGateway.save(newPlanWith(in));
        return new StdOutput(aPlan.id());
    }

    private Plan newPlanWith(final Input in) {
        return Plan.newPlan(
                this.planGateway.nextId(),
                in.name(), in.description(), in.active(),
                new Money(in.currency(), in.price())
        );
    }

    record StdOutput(PlanId planId) implements CreatePlan.Output {}
}