package io.github.gabrmsouza.subscription.infrastructure.configuration.usecases;

import io.github.gabrmsouza.subscription.application.plan.ChangePlan;
import io.github.gabrmsouza.subscription.application.plan.CreatePlan;
import io.github.gabrmsouza.subscription.application.plan.impl.DefaultChangePlan;
import io.github.gabrmsouza.subscription.application.plan.impl.DefaultCreatePlan;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class PlanUseCaseConfiguration {
    @Bean
    CreatePlan createPlan(PlanGateway planGateway) {
        return new DefaultCreatePlan(planGateway);
    }

    @Bean
    ChangePlan changePlan(PlanGateway planGateway) {
        return new DefaultChangePlan(planGateway);
    }
}
