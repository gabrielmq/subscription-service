package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.account.CreateIdpUser;
import io.github.gabrmsouza.subscription.domain.account.idp.IdentityProviderGateway;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DefaultCreateIdpUserTest extends UseCaseTest {
    @Mock
    private IdentityProviderGateway identityProviderGateway;

    @InjectMocks
    private DefaultCreateIdpUser target;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnUserID() {
        // given
        var expectedAccountId = "ACC-123";
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedPassword = "123456";
        var expectedUserId = new UserId("123");

        when(identityProviderGateway.create(any())).thenReturn(expectedUserId);

        // when
        final var input = new CreateIdpUserTestInput(
                expectedAccountId,
                expectedFirstname,
                expectedLastname,
                expectedEmail,
                expectedPassword
        );
        var actualOutput = this.target.execute(input);

        // then
        assertEquals(expectedUserId, actualOutput.idpUserId());
    }

    record CreateIdpUserTestInput(String accountId, String firstname, String lastname, String email, String password) implements CreateIdpUser.Input {
    }
}