package io.github.gabrmsouza.subscription.domain.subscription.status;

import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.UnitTest;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IncompleteSubscriptionStatusTest extends UnitTest {
    @Test
    public void givenInstance_whenCallsToString_shouldReturnValue() {
        // given
        var expectedString = "incomplete";
        var one = new IncompleteSubscriptionStatus(Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus()));

        // when
        var actualString = one.toString();

        // then
        assertEquals(expectedString, actualString);
    }

    @Test
    public void givenTwoInstances_whenCallsEquals_shouldBeEquals() {
        // given
        var expectedEquals = true;
        var one = new IncompleteSubscriptionStatus(Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus()));
        var two = new IncompleteSubscriptionStatus(Subscription.newSubscription(new SubscriptionId("1231"), new AccountId("ACC123"), Fixture.Plans.plus()));

        // when
        var actualEquals = one.equals(two);

        // then
        assertEquals(expectedEquals, actualEquals, "O equals deveria levar em conta apenas a classe do status e não a subscription");
    }

    @Test
    public void givenTwoInstances_whenCallsHashCode_shouldBeEquals() {
        // given
        var one = new IncompleteSubscriptionStatus(Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus()));
        var two = new IncompleteSubscriptionStatus(Subscription.newSubscription(new SubscriptionId("1231"), new AccountId("ACC123"), Fixture.Plans.plus()));

        // then
        assertEquals(one.hashCode(), two.hashCode(), "O hashCode deveria levar em conta apenas a classe do status e não a subscription");
    }

    @Test
    public void givenIncompleteStatus_whenCallsTrailing_shouldExpectError() {
        // given
        var expectedError = "Subscription with status incomplete can´t transit to trailing";
        var expectedStatusClass = IncompleteSubscriptionStatus.class;
        var expectedSubscription = incompleteSubscription();

        var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        var actualError = assertThrows(DomainException.class, () -> target.trailing());

        // then
        assertEquals(expectedError, actualError.getMessage());
        assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsCancel_shouldTransitToCanceledStatus() {
        // given
        var expectedStatusClass = CanceledSubscriptionStatus.class;
        var expectedSubscription = incompleteSubscription();
        var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        target.cancel();

        // then
        assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsActive_shouldTransitToActive() {
        // given
        var expectedStatusClass = ActiveSubscriptionStatus.class;
        var expectedSubscription = incompleteSubscription();
        var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        target.active();

        // then
        assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsIncomplete_shouldDoNothing() {
        // given
        var expectedStatusClass = IncompleteSubscriptionStatus.class;
        var expectedSubscription = incompleteSubscription();
        var target = new IncompleteSubscriptionStatus(expectedSubscription);

        // when
        target.incomplete();

        // then
        assertEquals(expectedStatusClass, expectedSubscription.status().getClass());
    }

    private static Subscription incompleteSubscription() {
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());
        expectedSubscription.status().incomplete();
        return expectedSubscription;
    }
}