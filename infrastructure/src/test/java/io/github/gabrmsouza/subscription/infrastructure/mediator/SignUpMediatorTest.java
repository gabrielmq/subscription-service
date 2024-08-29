package io.github.gabrmsouza.subscription.infrastructure.mediator;

import io.github.gabrmsouza.subscription.application.Presenter;
import io.github.gabrmsouza.subscription.application.account.CreateAccount;
import io.github.gabrmsouza.subscription.application.account.CreateIdpUser;
import io.github.gabrmsouza.subscription.domain.UnitTest;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import io.github.gabrmsouza.subscription.domain.person.Document;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.SignUpRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.SignUpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SignUpMediatorTest extends UnitTest {
    @Mock
    private AccountGateway accountGateway;

    @Mock
    private CreateAccount createAccount;

    @Mock
    private CreateIdpUser createIdpUser;

    @InjectMocks
    private SignUpMediator signUpMediator;

    @Captor
    private ArgumentCaptor<CreateAccount.Input> createAccountInputCaptor;

    @Test
    void givenValidRequest_whenSignUpSuccessfully_shouldReturnAccountId() {
        // given
        var expectedFirstname = "John";
        var expectedLastname = "Doe";
        var expectedEmail = "john@gmail.com";
        var expectedDocumentType = Document.Cpf.TYPE;
        var expectedDocumentNumber = "12312312323";
        var expectedPassword = "123";
        var expectedUserId = new UserId("123");
        var expectedAccountId = new AccountId("ACC-123");

        var req = new SignUpRequest(expectedDocumentNumber, expectedDocumentType, expectedPassword, expectedEmail, expectedLastname, expectedFirstname);

        when(accountGateway.nextId()).thenReturn(expectedAccountId);

        when(createIdpUser.execute(any(), any())).thenAnswer(t -> {
            final Presenter<CreateIdpUser.Output, SignUpRequest> a2 = t.getArgument(1);
            return a2.apply(() -> expectedUserId);
        });

        when(createAccount.execute(any(), any())).thenAnswer(t -> {
            final Presenter<CreateAccount.Output, SignUpResponse> a2 = t.getArgument(1);
            return a2.apply(() -> expectedAccountId);
        });

        // when
        var actualOutput = this.signUpMediator.signUp(req);

        // then
        assertEquals(expectedAccountId.value(), actualOutput.accountId());

        verify(createAccount).execute(createAccountInputCaptor.capture(), any());

        var actualInput = createAccountInputCaptor.getValue();
        assertEquals(expectedUserId.value(), actualInput.userId());
        assertEquals(expectedAccountId.value(), actualInput.accountId());
    }
}