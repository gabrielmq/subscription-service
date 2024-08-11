package io.github.gabrmsouza.subscription.domain.account;

import io.github.gabrmsouza.subscription.domain.UnitTest;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountIdTest extends UnitTest {
    @Test
    public void givenValidId_whenInstantiate_shouldReturnVO() {
        // given
        var expectedAccountId = "123";

        // when
        var actualUserId = new AccountId(expectedAccountId);

        // then
        assertEquals(expectedAccountId, actualUserId.value());
    }

    @Test
    public void givenEmptyAccountId_whenInstantiate_shouldReturnError() {
        // given
        var expectedAccountId = "";
        var expectedError = "'accountId' should not be empty";

        // when
        var actualError = assertThrows(DomainException.class, () -> new AccountId(expectedAccountId));

        // then
        assertEquals(expectedError, actualError.getMessage());
    }
}