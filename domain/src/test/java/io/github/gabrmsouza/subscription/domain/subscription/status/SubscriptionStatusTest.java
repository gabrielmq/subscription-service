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

class SubscriptionStatusTest extends UnitTest {
    @Test
    public void givenUnexpectedStatus_whenCallsCreate_shouldThrowDomainException() {
        // given
        var expectedStatus = "a";
        var expectedErrorMessage = "Invalid status: a";
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());

        // when
        var actualError = assertThrows(DomainException.class, () -> SubscriptionStatus.create(expectedStatus, expectedSubscription));

        // then
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    public void givenNullStatus_whenCallsCreate_shouldThrowDomainException() {
        // given
        String expectedStatus = null;
        var expectedErrorMessage = "'status' should not be null";
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());

        // when
        var actualError = assertThrows(DomainException.class, () -> SubscriptionStatus.create(expectedStatus, expectedSubscription));

        // then
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    public void givenNullSubscription_whenCallsCreate_shouldThrowDomainException() {
        // given
        var expectedStatus = "incomplete";
        var expectedErrorMessage = "'subscription' should not be null";

        // when
        var actualError = assertThrows(DomainException.class, () -> SubscriptionStatus.create(expectedStatus, null));

        // then
        assertEquals(expectedErrorMessage, actualError.getMessage());
    }

    @Test
    public void givenTrailingStatus_whenCallsCreate_shouldInstantiateTrailingSubscriptionStatus() {
        // given
        var expectedStatus = "trailing";
        var expectedStatusClass = TrailingSubscriptionStatus.class;
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());

        // when
        var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        assertEquals(expectedStatus, actualStatus.value());
        assertEquals(expectedStatusClass, actualStatus.getClass());
    }

    @Test
    public void givenCanceledStatus_whenCallsCreate_shouldInstantiateCanceledSubscriptionStatus() {
        // given
        var expectedStatus = "canceled";
        var expectedStatusClass = CanceledSubscriptionStatus.class;
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());

        // when
        var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        assertEquals(expectedStatus, actualStatus.value());
        assertEquals(expectedStatusClass, actualStatus.getClass());
    }

    @Test
    public void givenActiveStatus_whenCallsCreate_shouldInstantiateActiveSubscriptionStatus() {
        // given
        var expectedStatus = "active";
        var expectedStatusClass = ActiveSubscriptionStatus.class;
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());

        // when
        var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        assertEquals(expectedStatus, actualStatus.value());
        assertEquals(expectedStatusClass, actualStatus.getClass());
    }

    @Test
    public void givenIncompleteStatus_whenCallsCreate_shouldInstantiateIncompleteSubscriptionStatus() {
        // given
        var expectedStatus = "incomplete";
        var expectedStatusClass = IncompleteSubscriptionStatus.class;
        var expectedSubscription = Subscription.newSubscription(new SubscriptionId("SUB"), new AccountId("ACC123"), Fixture.Plans.plus());

        // when
        var actualStatus = SubscriptionStatus.create(expectedStatus, expectedSubscription);

        // then
        assertEquals(expectedStatus, actualStatus.value());
        assertEquals(expectedStatusClass, actualStatus.getClass());
    }
}