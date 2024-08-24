package io.github.gabrmsouza.subscription.application.plan.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.plan.CreatePlan;
import io.github.gabrmsouza.subscription.domain.money.Money;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultCreatePlanTest extends UseCaseTest {
    @Mock
    private PlanGateway planGateway;

    @InjectMocks
    private DefaultCreatePlan target;

    @Captor
    private ArgumentCaptor<Plan> captor;

    @Test
    public void givenValidInput_whenCallsExecute_shouldCreatePlan() {
        // given
        var expectedName = "Plus";
        var expectedDescription = "O melhor plano";
        var expectedPrice = 20d;
        var expectedCurrency = "USD";
        var expectedActive = true;
        var expectedPlanId = new PlanId(999L);

        when(planGateway.nextId()).thenReturn(expectedPlanId);
        when(planGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput =
                this.target.execute(new CreatePlanTestInput(expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive));

        // then
        Assertions.assertEquals(expectedPlanId, actualOutput.planId());

        verify(planGateway).save(captor.capture());

        var actualPlan = captor.getValue();
        Assertions.assertEquals(expectedPlanId, actualPlan.id());
        Assertions.assertEquals(expectedName, actualPlan.name());
        Assertions.assertEquals(expectedDescription, actualPlan.description());
        Assertions.assertEquals(new Money(expectedCurrency, expectedPrice), actualPlan.price());
        Assertions.assertEquals(expectedActive, actualPlan.active());
    }

    record CreatePlanTestInput(
            String name,
            String description,
            Double price,
            String currency,
            Boolean active
    ) implements CreatePlan.Input {

    }
}