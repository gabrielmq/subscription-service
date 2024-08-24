package io.github.gabrmsouza.subscription.application.plan.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.plan.ChangePlan;
import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.money.Money;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultChangePlanTest extends UseCaseTest {
    @Mock
    private PlanGateway planGateway;

    @InjectMocks
    private DefaultChangePlan target;

    @Captor
    private ArgumentCaptor<Plan> captor;

    @Test
    public void givenValidInput_whenCallsExecute_shouldChangePlan() {
        // given
        var plan = Fixture.Plans.plus();
        var expectedName = "Plan Master";
        var expectedDescription = "Master plan";
        var expectedPrice = 660d;
        var expectedCurrency = "USD";
        var expectedActive = true;
        var expectedPlanId = plan.id();

        when(planGateway.planOfId(any())).thenReturn(Optional.of(plan));
        when(planGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput =
                this.target.execute(new ChangePlanTestInput(expectedPlanId.value(), expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive));

        // then
        assertEquals(expectedPlanId, actualOutput.planId());

        verify(planGateway).save(captor.capture());

        var actualPlan = captor.getValue();
        assertEquals(expectedPlanId, actualPlan.id());
        assertEquals(expectedName, actualPlan.name());
        assertEquals(expectedDescription, actualPlan.description());
        assertEquals(new Money(expectedCurrency, expectedPrice), actualPlan.price());
        assertEquals(expectedActive, actualPlan.active());
    }

    record ChangePlanTestInput(
            Long planId,
            String name,
            String description,
            Double price,
            String currency,
            Boolean active
    ) implements ChangePlan.Input {

    }
}