package io.github.gabrmsouza.subscription.application.subscription.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.subscription.CancelSubscription;
import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import io.github.gabrmsouza.subscription.domain.subscription.status.ActiveSubscriptionStatus;
import io.github.gabrmsouza.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DefaultCancelSubscriptionTest extends UseCaseTest {
    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultCancelSubscription target;

    @Test
    public void givenActiveSubscription_whenCallsCancelSubscription_shouldCancelIt() {
        // given
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, ActiveSubscriptionStatus.ACTIVE, LocalDateTime.now().minusDays(15));
        var expectedSubscriptionId = expectedSubscription.id();
        var expectedSubscriptionStatus = CanceledSubscriptionStatus.CANCELED;

        when(subscriptionGateway.latestSubscriptionOfAccount(eq(expectedAccount.id()))).thenReturn(Optional.of(expectedSubscription));
        when(subscriptionGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        var actualOutput =
                this.target.execute(new CancelSubscriptionTestInput(expectedSubscriptionId.value(), expectedAccount.id().value()));

        // then
        assertEquals(expectedSubscriptionId, actualOutput.subscriptionId());
        assertEquals(expectedSubscriptionStatus, actualOutput.subscriptionStatus());
    }

    private static Subscription newSubscriptionWith(AccountId expectedAccountId, Plan plus, String status, LocalDateTime date) {
        final var instant = date.toInstant(ZoneOffset.UTC);
        return Subscription.with(
                new SubscriptionId("SUB123"), 1, expectedAccountId, plus.id(),
                date.toLocalDate(), status,
                instant, "a123",
                instant, instant
        );
    }

    record CancelSubscriptionTestInput(String subscriptionId, String accountId) implements CancelSubscription.Input {
    }
}