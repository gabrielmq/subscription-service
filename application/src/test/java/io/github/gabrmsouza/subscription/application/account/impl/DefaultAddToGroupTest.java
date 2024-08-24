package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.account.AddToGroup;
import io.github.gabrmsouza.subscription.domain.Fixture;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.idp.GroupId;
import io.github.gabrmsouza.subscription.domain.account.idp.IdentityProviderGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultAddToGroupTest extends UseCaseTest {
    @Mock
    private AccountGateway accountGateway;

    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @Mock
    private SubscriptionGateway subscriptionGateway;

    @InjectMocks
    private DefaultAddToGroup target;

    @Test
    public void givenTrailSubscription_whenCallsExecute_shouldCallIdentityProvider() {
        // given
        final var john = Fixture.Accounts.john();
        final var expectedGroupId = new GroupId("GROUP-123");
        final var expectedAccountId = john.id();
        final var expectedSubscriptionId = new SubscriptionId("SUB-123");
        final var johnsSubscription = Fixture.Subscriptions.johns();

        assertTrue(johnsSubscription.isTrail(), "Para esse teste a subscription precisa estar com Trial");

        when(subscriptionGateway.subscriptionOfId(any())).thenReturn(Optional.of(johnsSubscription));
        when(accountGateway.accountOfId(any())).thenReturn(Optional.of(john));
        doNothing().when(identityProviderGateway).addUserToGroup(any(), any());

        // when
        this.target.execute(new AddToGroupTestInput(expectedAccountId.value(), expectedGroupId.value(), expectedSubscriptionId.value()));

        // then
        verify(subscriptionGateway).subscriptionOfId(eq(expectedSubscriptionId));
        verify(accountGateway).accountOfId(eq(john.id()));
        verify(identityProviderGateway).addUserToGroup(eq(john.userId()), eq(expectedGroupId));
    }

    record AddToGroupTestInput(
            String accountId,
            String groupId,
            String subscriptionId
    ) implements AddToGroup.Input {
    }
}