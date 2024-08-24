package io.github.gabrmsouza.subscription.application.plan;

import io.github.gabrmsouza.subscription.application.UseCase;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;

public abstract class CreatePlan extends UseCase<CreatePlan.Input, CreatePlan.Output> {
    public interface Input {
        String name();
        String description();
        Double price();
        String currency();
        Boolean active();
    }

    public interface Output {
        PlanId planId();
    }
}
