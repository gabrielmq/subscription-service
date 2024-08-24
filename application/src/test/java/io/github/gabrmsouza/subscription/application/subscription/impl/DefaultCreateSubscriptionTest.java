package io.github.gabrmsouza.subscription.application.subscription.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.subscription.CreateSubscription;
import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultCreateSubscriptionTest extends UseCaseTest {
    @Mock
    private AccountGateway accountGateway;

    @Mock
    private PlanGateway planGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultCreateSubscription target;

    @Captor
    private ArgumentCaptor<Subscription> captor;

    @Test
    public void givenValidAccountAndPlan_whenCallsCreateSubscription_shouldReturnNewSubscription() {
        // given
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedSubscriptionId = new SubscriptionId("SUB123");

        when(this.subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.empty());
        when(this.planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(this.accountGateway.accountOfId(any())).thenReturn(Optional.of(expectedAccount));
        when(this.subscriptionGateway.nextId()).thenReturn(expectedSubscriptionId);
        when(this.subscriptionGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualOutput =
                this.target.execute(new CreateSubscriptionTestInput(expectedPlan.id().value(), expectedAccount.id().value()));

        // then
        assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());

        verify(subscriptionGateway).save(captor.capture());

        final var actualSubscription = captor.getValue();
        assertEquals(expectedSubscriptionId, actualSubscription.id());
        assertEquals(expectedPlan.id(), actualSubscription.planId());
        assertEquals(expectedAccount.id(), actualSubscription.accountId());
        assertTrue(actualSubscription.isTrail());
        assertNotNull(actualSubscription.dueDate());
    }

    record CreateSubscriptionTestInput(Long planId, String accountId) implements CreateSubscription.Input {
    }
}