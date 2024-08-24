package io.github.gabrmsouza.subscription.application.plan.impl;

import io.github.gabrmsouza.subscription.application.plan.ChangePlan;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.domain.money.Money;
import io.github.gabrmsouza.subscription.domain.plan.PlanCommand;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;

import java.util.Objects;

public class DefaultChangePlan extends ChangePlan {
    private final PlanGateway planGateway;

    public DefaultChangePlan(final PlanGateway planGateway) {
        this.planGateway = Objects.requireNonNull(planGateway);
    }

    @Override
    public ChangePlan.Output execute(final ChangePlan.Input in) {
        final var aPlan = this.planGateway.planOfId(new PlanId(in.planId()))
                .orElseThrow(() -> DomainException.with("Plan with id %s could not be found".formatted(in.planId())));

        aPlan.execute(new PlanCommand.ChangePlan(in.name(), in.description(), new Money(in.currency(), in.price()), in.active()));
        this.planGateway.save(aPlan);
        return new StdOutput(aPlan.id());
    }

    record StdOutput(PlanId planId) implements Output {}
}