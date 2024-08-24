package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.account.RemoveFromGroup;
import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.GroupId;
import io.github.gabrmsouza.subscription.domain.account.idp.IdentityProviderGateway;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import io.github.gabrmsouza.subscription.domain.subscription.status.CanceledSubscriptionStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultRemoveFromGroupTest extends UseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultRemoveFromGroup target;

    @Test
    public void givenCanceledSubscriptionAndDueDatePast_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var expectedGroupId = new GroupId("GROUP-123");
        final var plus = Fixture.Plans.plus();
        final var john = Fixture.Accounts.john();
        final var expectedAccountId = john.id();
        final var johnsSubscription = newSubscriptionWith(expectedAccountId, plus, CanceledSubscriptionStatus.CANCELED, LocalDateTime.now().minusDays(1));
        final var expectedSubscriptionId = johnsSubscription.id();

        assertTrue(johnsSubscription.isCanceled(), "Para esse teste a subscription precisa estar com Canceled status");

        when(subscriptionGateway.subscriptionOfId(any())).thenReturn(Optional.of(johnsSubscription));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(john));
        doNothing().when(identityProviderGateway).removeUserFromGroup(any(), any());

        // when
        this.target.execute(new RemoveFromGroupTestInput(expectedAccountId.value(), expectedGroupId.value(), expectedSubscriptionId.value()));

        // then
        verify(subscriptionGateway).subscriptionOfId(eq(expectedSubscriptionId));
        verify(accountGateway).accountOfId(eq(john.id()));
        verify(identityProviderGateway).removeUserFromGroup(eq(john.userId()), eq(expectedGroupId));
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

    record RemoveFromGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements RemoveFromGroup.Input {

    }
}