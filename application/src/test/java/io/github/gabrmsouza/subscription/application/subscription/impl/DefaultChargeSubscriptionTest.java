package io.github.gabrmsouza.subscription.application.subscription.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.subscription.ChargeSubscription;
import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.payment.Payment;
import io.github.gabrmsouza.subscription.domain.payment.PaymentGateway;
import io.github.gabrmsouza.subscription.domain.payment.PixPayment;
import io.github.gabrmsouza.subscription.domain.payment.Transaction;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import io.github.gabrmsouza.subscription.domain.subscription.status.SubscriptionStatus;
import io.github.gabrmsouza.subscription.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DefaultChargeSubscriptionTest extends UseCaseTest {
    @Mock
    private AccountGateway accountGateway;

    @Mock
    private Clock clock;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private PlanGateway planGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultChargeSubscription target;

    @Captor
    private ArgumentCaptor<Payment> paymentCaptor;

    @Test
    public void givenSubscriptionOutOfChargePeriod_whenCallsChargeSubscription_shouldSkipCharges() {
        // given
        var referenceDate = LocalDateTime.now().plusDays(2);
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, expectedStatus, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate();

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(eq(expectedAccount.id()))).thenReturn(Optional.of(expectedSubscription));

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), expectedSubscription.id().value(), Payment.PIX, null));

        // then
        assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        Assertions.assertNull(actualOutput.paymentTransaction());
    }

    @Test
    public void givenSubscriptionWithPastDueDate_whenChargeSuccessfully_shouldSaveAsActive() {
        // given
        var referenceDate = LocalDateTime.now().minusDays(1);
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.INCOMPLETE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate().plusMonths(1);
        var expectedTransaction = Transaction.success("TRS-123");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(expectedAccount));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), expectedSubscription.id().value(), Payment.PIX, null));

        // then
        assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway).planOfId(eq(expectedPlan.id()));
        verify(accountGateway).accountOfId(eq(expectedAccount.id()));
        verify(subscriptionGateway).save(any());
        verify(paymentGateway).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        assertInstanceOf(PixPayment.class, actualPayment);
        assertNotNull(actualPayment.orderId());
        assertEquals(expectedPlan.price().amount(), actualPayment.amount());
    }

    @Test
    public void givenSubscriptionWithDueDateNow_whenChargeSuccessfully_shouldSaveAsActive() {
        // given
        var referenceDate = LocalDateTime.now();
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.ACTIVE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.ACTIVE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate().plusMonths(1);
        var expectedTransaction = Transaction.success("TRS-123");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(expectedAccount));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), expectedSubscription.id().value(), Payment.PIX, null));

        // then
        assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway).planOfId(eq(expectedPlan.id()));
        verify(accountGateway).accountOfId(eq(expectedAccount.id()));
        verify(subscriptionGateway).save(any());
        verify(paymentGateway).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        assertInstanceOf(PixPayment.class, actualPayment);
        assertNotNull(actualPayment.orderId());
        assertEquals(expectedPlan.price().amount(), actualPayment.amount());
    }

    @Test
    public void givenSubscriptionWithDueDateNow_whenChargeFailure_shouldSaveAsIncomplete() {
        // given
        var referenceDate = LocalDateTime.now();
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.INCOMPLETE;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.ACTIVE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate();
        var expectedTransaction = Transaction.failure("TRS-123", "No funds");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(expectedAccount));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), expectedSubscription.id().value(), Payment.PIX, null));

        // then
        assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway).planOfId(eq(expectedPlan.id()));
        verify(accountGateway).accountOfId(eq(expectedAccount.id()));
        verify(subscriptionGateway).save(any());
        verify(paymentGateway).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        assertInstanceOf(PixPayment.class, actualPayment);
        assertNotNull(actualPayment.orderId());
        assertEquals(expectedPlan.price().amount(), actualPayment.amount());
        verify(subscriptionGateway).save(any());
    }

    @Test
    public void givenSubscriptionWithPastDueDate_whenChargeFailureAndPastMaxIncompleteDays_shouldSaveAsCanceled() {
        // given
        var referenceDate = LocalDateTime.now().minusDays(4);
        var expectedPlan = Fixture.Plans.plus();
        var expectedAccount = Fixture.Accounts.john();
        var expectedStatus = SubscriptionStatus.CANCELED;
        var expectedSubscription = newSubscriptionWith(expectedAccount.id(), expectedPlan, SubscriptionStatus.INCOMPLETE, referenceDate);
        var expectedDueDate = referenceDate.toLocalDate();
        var expectedTransaction = Transaction.failure("TRS-555", "No funds");

        when(clock.instant()).thenReturn(InstantUtils.now());
        when(subscriptionGateway.latestSubscriptionOfAccount(any())).thenReturn(Optional.of(expectedSubscription));
        when(planGateway.planOfId(any())).thenReturn(Optional.of(expectedPlan));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(expectedAccount));
        when(paymentGateway.processPayment(any())).thenReturn(expectedTransaction);

        // when
        var actualOutput =
                this.target.execute(new ChargeSubscriptionTestInput(expectedAccount.id().value(), expectedSubscription.id().value(), Payment.PIX, null));

        // then
        assertEquals(expectedSubscription.id(), actualOutput.subscriptionId());
        assertEquals(expectedStatus, actualOutput.subscriptionStatus());
        assertEquals(expectedDueDate, actualOutput.subscriptionDueDate());
        assertEquals(expectedTransaction, actualOutput.paymentTransaction());

        verify(subscriptionGateway).latestSubscriptionOfAccount(eq(expectedAccount.id()));
        verify(planGateway).planOfId(eq(expectedPlan.id()));
        verify(accountGateway).accountOfId(eq(expectedAccount.id()));
        verify(subscriptionGateway).save(any());
        verify(paymentGateway).processPayment(paymentCaptor.capture());

        var actualPayment = paymentCaptor.getValue();
        assertInstanceOf(PixPayment.class, actualPayment);
        assertNotNull(actualPayment.orderId());
        assertEquals(expectedPlan.price().amount(), actualPayment.amount());
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

    record ChargeSubscriptionTestInput(
            String accountId,
            String subscriptionId,
            String paymentType,
            String creditCardToken
    ) implements ChargeSubscription.Input {}
}