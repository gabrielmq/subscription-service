package io.github.gabrmsouza.subscription.application.account.impl;

import io.github.gabrmsouza.subscription.application.UseCaseTest;
import io.github.gabrmsouza.subscription.application.account.CreateAccount;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import io.github.gabrmsouza.subscription.domain.person.Document;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DefaultCreateAccountTest extends UseCaseTest {
    @Mock
    private AccountGateway accountGateway;

    @InjectMocks
    private DefaultCreateAccount target;

    @Test
    public void givenValidInput_whenCallsExecute_shouldReturnAccountID() {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedDocumentType = Document.Cpf.TYPE;
        var expectedDocumentNumber = "12312312323";
        var expectedUserId = new UserId("123");
        var expectedAccountId = new AccountId("ACC-123");

        when(accountGateway.save(any())).thenAnswer(returnsFirstArg());

        // when
        final var input = new CreateAccountTestInput(
                expectedUserId.value(),
                expectedAccountId.value(),
                expectedFirstname,
                expectedLastname,
                expectedEmail,
                expectedDocumentType,
                expectedDocumentNumber
        );
        var actualOutput = this.target.execute(input);

        // then
        assertEquals(expectedAccountId, actualOutput.accountId());
    }

    record CreateAccountTestInput(
            String userId,
            String accountId,
            String firstname,
            String lastname,
            String email,
            String documentType,
            String documentNumber
    ) implements CreateAccount.Input {}
}