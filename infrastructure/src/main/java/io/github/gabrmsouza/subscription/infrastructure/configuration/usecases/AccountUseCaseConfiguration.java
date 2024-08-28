package io.github.gabrmsouza.subscription.infrastructure.configuration.usecases;

import io.github.gabrmsouza.subscription.application.account.*;
import io.github.gabrmsouza.subscription.application.account.impl.*;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.account.idp.IdentityProviderGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountUseCaseConfiguration {
    @Bean
    CreateAccount createAccount(final AccountGateway accountGateway) {
        return new DefaultCreateAccount(accountGateway);
    }

    @Bean
    CreateIdpUser createIdpUser(final IdentityProviderGateway identityProviderGateway) {
        return new DefaultCreateIdpUser(identityProviderGateway);
    }

    @Bean
    AddToGroup addToGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultAddToGroup(accountGateway, identityProviderGateway, subscriptionGateway);
    }

    @Bean
    RemoveFromGroup removeFromGroup(
            final AccountGateway accountGateway,
            final IdentityProviderGateway identityProviderGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultRemoveFromGroup(accountGateway, identityProviderGateway, subscriptionGateway);
    }

    @Bean
    UpdateBillingInfo updateBillingInfo(final AccountGateway accountGateway) {
        return new DefaultUpdateBillingInfo(accountGateway);
    }
}
