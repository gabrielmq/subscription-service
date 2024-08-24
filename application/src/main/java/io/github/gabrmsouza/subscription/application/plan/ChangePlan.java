package io.github.gabrmsouza.subscription.application.plan;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;

public abstract class ChangePlan extends UseCase<ChangePlan.Input, ChangePlan.Output> {
    public interface Input {
        Long planId();
        String name();
        String description();
        String currency();
        Double price();
        Boolean active();
    }

    public interface Output {
        PlanId planId();
    }
}