package io.github.gabrmsouza.subscription.domain.account;

import io.github.gabrmsouza.subscription.domain.UnitTest;
import io.github.gabrmsouza.subscription.domain.account.iam.UserId;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.domain.person.Address;
import io.github.gabrmsouza.subscription.domain.person.Document;
import io.github.gabrmsouza.subscription.domain.person.Email;
import io.github.gabrmsouza.subscription.domain.person.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest extends UnitTest {
    @Test
    void givenValidParams_whenCallsNewAccount_thenShouldInstantiate() {
        // given
        final var expectedId = new AccountId("acc123");
        final var expectedUserId = new UserId("user-123");
        final var expectedName = new Name("John", "Doe");
        final var expectedVersion = 0;
        final var expectedEmail = new Email("john@test.com");
        final var expectedDocument = Document.create("12345678912", "cpf");

        final var expectedEvents = 1;

        // when
        final var actualAccount = Account.newAccount(
                expectedId,
                expectedUserId,
                expectedEmail,
                expectedName,
                expectedDocument
        );

        // then
        assertNotNull(actualAccount);
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedDocument, actualAccount.document());
        assertNull(actualAccount.billingAddress());

        Assertions.assertEquals(expectedEvents, actualAccount.domainEvents().size());
        Assertions.assertInstanceOf(AccountCreated.class, actualAccount.domainEvents().getFirst());
    }

    @Test
    void givenValidParams_whenCallsWith_thenShouldInstantiate() {
        // given
        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualAccount = Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress);

        // then
        assertNotNull(actualAccount);
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedDocument, actualAccount.document());
        assertEquals(expectedAddress, actualAccount.billingAddress());
    }

    @Test
    public void givenInvalidId_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'id' should not be null";

        AccountId expectedId = null;
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenInvalidUserId_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'userId' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        UserId expectedUserId = null;
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenInvalidName_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'name' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        Name expectedName = null;
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenInvalidEmail_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'email' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        Email expectedEmail = null;
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenInvalidDocument_whenCallsWith_ShouldReturnError() {
        // given
        var expectedMessage = "'document' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        Document expectedDocument = null;
        var expectedAddress = new Address("09123123", "11", "Bloco A", "BR");

        // when
        var actualError = assertThrows(
                DomainException.class,
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );

        // then
        assertEquals(expectedMessage, actualError.getMessage());
    }

    @Test
    public void givenNullAddress_whenCallsWith_ShouldReturnOK() {
        // given
        var expectedMessage = "'id' should not be null";

        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 1;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        Address expectedAddress = null;

        // when
        assertDoesNotThrow(
                () -> Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, expectedDocument, expectedAddress)
        );
    }

    @Test
    public void givenAccount_whenCallsExecuteWithProfileCommand_ShouldUpdateNameAndAddress() {
        // given
        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 0;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("12312123", "123", null, "BR");
        var expectedEvents = 0;

        var actualAccount = Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, new Name("Valentin", "Doe"), expectedDocument, null);

        // when
        actualAccount.execute(new AccountCommand.ChangeProfile(expectedName, expectedAddress));

        // then
        assertNotNull(actualAccount);
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedDocument, actualAccount.document());
        assertEquals(expectedAddress, actualAccount.billingAddress());

        assertEquals(expectedEvents, actualAccount.domainEvents().size());
    }

    @Test
    public void givenAccount_whenCallsExecuteWithEmailCommand_ShouldUpdateEmail() {
        // given
        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 0;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("12312123", "123", null, "BR");
        var expectedEvents = 0;

        var actualAccount = Account.with(expectedId, expectedVersion, expectedUserId, new Email("valentin@gmail.com"), expectedName, expectedDocument, expectedAddress);

        // when
        actualAccount.execute(new AccountCommand.ChangeEmail(expectedEmail));

        // then
        assertNotNull(actualAccount);
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedDocument, actualAccount.document());
        assertEquals(expectedAddress, actualAccount.billingAddress());

        assertEquals(expectedEvents, actualAccount.domainEvents().size());
    }

    @Test
    public void givenAccount_whenCallsExecuteWithDocumentCommand_ShouldUpdateDocument() {
        // given
        var expectedId = new AccountId("ACC-123");
        var expectedVersion = 0;
        var expectedUserId = new UserId("USER-123");
        var expectedName = new Name("John", "Doe");
        var expectedEmail = new Email("john@gmail.com");
        var expectedDocument = Document.create("12345678912", "cpf");
        var expectedAddress = new Address("12312123", "123", null, "BR");
        var expectedEvents = 0;

        var actualAccount = Account.with(expectedId, expectedVersion, expectedUserId, expectedEmail, expectedName, Document.create("12345673333", "cpf"), expectedAddress);

        // when
        actualAccount.execute(new AccountCommand.ChangeDocument(expectedDocument));

        // then
        assertNotNull(actualAccount);
        assertEquals(expectedId, actualAccount.id());
        assertEquals(expectedVersion, actualAccount.version());
        assertEquals(expectedUserId, actualAccount.userId());
        assertEquals(expectedName, actualAccount.name());
        assertEquals(expectedEmail, actualAccount.email());
        assertEquals(expectedDocument, actualAccount.document());
        assertEquals(expectedAddress, actualAccount.billingAddress());

        assertEquals(expectedEvents, actualAccount.domainEvents().size());
    }
}