package io.github.gabrmsouza.subscription;

import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class ControllerTestConfiguration {
    @Bean
    public AccountGateway accountGateway() {
        return Mockito.mock(AccountGateway.class);
    }
}
