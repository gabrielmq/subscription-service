package io.github.gabrmsouza.subscription.domain.subscription;

import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.UnitTest;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;
import io.github.gabrmsouza.subscription.domain.subscription.status.SubscriptionStatus;
import io.github.gabrmsouza.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest extends UnitTest {
    @Test
    public void givenValidParams_whenCallsNewSubscription_ShouldInstantiate() {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlan = Fixture.Plans.plus();
        var expectedStatus = SubscriptionStatus.TRAILING;
        var expectedDueDate = LocalDate.now().plusMonths(1);
        Instant expectedLastRenewDate = null;
        String expectedLastTransactionId = null;
        var expectedEvents = 1;

        // when
        var actualSubscription =
                Subscription.newSubscription(expectedId, expectedAccountId, expectedPlan);

        // then
        assertNotNull(actualSubscription);
        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlan.id(), actualSubscription.planId());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        assertNotNull(actualSubscription.createdAt());
        assertNotNull(actualSubscription.updatedAt());

        assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        assertInstanceOf(SubscriptionCreated.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenValidParams_whenCallsWith_ShouldInstantiate() {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = new PlanId(123L);
        var expectedStatus = SubscriptionStatus.TRAILING;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now().plusMonths(1);
        var expectedLastRenewDate = InstantUtils.now().minus(7, ChronoUnit.DAYS);
        var expectedLastTransactionId = UUID.randomUUID().toString();

        // when
        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDueDate,
                expectedStatus,
                expectedLastRenewDate,
                expectedLastTransactionId,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // then
        assertNotNull(actualSubscription);
        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        assertEquals(expectedUpdatedAt, actualSubscription.updatedAt());

        Assertions.assertTrue(actualSubscription.domainEvents().isEmpty());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteIncompleteCommand_ShouldTransitToIncompleteState() {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = new PlanId(123L);
        var expectedStatus = SubscriptionStatus.INCOMPLETE;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now();
        Instant expectedLastRenewDate = null;
        var expectedLastTransactionId = UUID.randomUUID().toString();
        var expectedReason = "Fail to charge creditcard";
        var expectedEvents = 1;

        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDueDate,
                SubscriptionStatus.TRAILING,
                expectedLastRenewDate,
                null,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new SubscriptionCommand.IncompleteSubscription(expectedReason, expectedLastTransactionId));

        // then
        assertNotNull(actualSubscription);
        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertEquals(expectedLastRenewDate, actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        assertInstanceOf(SubscriptionIncomplete.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteRenewCommand_ShouldTransitToActiveState() {
        // given
        var expectedPlan = Fixture.Plans.plus();

        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = expectedPlan.id();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now().plusMonths(1);
        var expectedLastTransactionId = UUID.randomUUID().toString();
        var expectedEvents = 1;

        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                LocalDate.now(),
                SubscriptionStatus.TRAILING,
                null,
                null,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        actualSubscription.execute(new SubscriptionCommand.RenewSubscription(expectedPlan, expectedLastTransactionId));

        // then
        assertNotNull(actualSubscription);
        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertNotNull(actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        assertInstanceOf(SubscriptionRenewed.class, actualSubscription.domainEvents().getFirst());
    }

    @Test
    public void givenTrialingSubscription_whenExecuteCancelCommand_ShouldTransitToCanceledState() throws InterruptedException {
        // given
        var expectedId = new SubscriptionId("SUB123");
        var expectedVersion = 0;
        var expectedAccountId = new AccountId("ACC123");
        var expectedPlanId = new PlanId(123L);
        var expectedStatus = SubscriptionStatus.CANCELED;
        var expectedCreatedAt = InstantUtils.now();
        var expectedUpdatedAt = InstantUtils.now();
        var expectedDueDate = LocalDate.now().plusMonths(1);
        var expectedLastRenewDate = InstantUtils.now();
        var expectedLastTransactionId = UUID.randomUUID().toString();
        var expectedEvents = 1;

        var actualSubscription = Subscription.with(
                expectedId,
                expectedVersion,
                expectedAccountId,
                expectedPlanId,
                expectedDueDate,
                SubscriptionStatus.TRAILING,
                expectedLastRenewDate,
                expectedLastTransactionId,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        // when
        Thread.sleep(1);
        actualSubscription.execute(new SubscriptionCommand.CancelSubscription());

        // then
        assertNotNull(actualSubscription);
        assertEquals(expectedId, actualSubscription.id());
        assertEquals(expectedVersion, actualSubscription.version());
        assertEquals(expectedAccountId, actualSubscription.accountId());
        assertEquals(expectedPlanId, actualSubscription.planId());
        assertEquals(expectedDueDate, actualSubscription.dueDate());
        assertEquals(expectedStatus, actualSubscription.status().value());
        assertNotNull(actualSubscription.lastRenewDate());
        assertEquals(expectedLastTransactionId, actualSubscription.lastTransactionId());
        assertEquals(expectedCreatedAt, actualSubscription.createdAt());
        Assertions.assertTrue(actualSubscription.updatedAt().isAfter(expectedUpdatedAt));

        assertEquals(expectedEvents, actualSubscription.domainEvents().size());
        assertInstanceOf(SubscriptionCanceled.class, actualSubscription.domainEvents().getFirst());
    }
}