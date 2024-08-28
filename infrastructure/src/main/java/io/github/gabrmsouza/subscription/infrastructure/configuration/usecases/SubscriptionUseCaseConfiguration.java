package io.github.gabrmsouza.subscription.infrastructure.configuration.usecases;

import io.github.gabrmsouza.subscription.application.subscription.CancelSubscription;
import io.github.gabrmsouza.subscription.application.subscription.ChargeSubscription;
import io.github.gabrmsouza.subscription.application.subscription.CreateSubscription;
import io.github.gabrmsouza.subscription.application.subscription.impl.DefaultCancelSubscription;
import io.github.gabrmsouza.subscription.application.subscription.impl.DefaultChargeSubscription;
import io.github.gabrmsouza.subscription.application.subscription.impl.DefaultCreateSubscription;
import io.github.gabrmsouza.subscription.domain.account.AccountGateway;
import io.github.gabrmsouza.subscription.domain.payment.PaymentGateway;
import io.github.gabrmsouza.subscription.domain.plan.PlanGateway;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration(proxyBeanMethods = false)
public class SubscriptionUseCaseConfiguration {
    @Bean
    CreateSubscription createSubscription(
            final AccountGateway accountGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCreateSubscription(accountGateway, planGateway, subscriptionGateway);
    }

    @Bean
    CancelSubscription cancelSubscription(
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultCancelSubscription(subscriptionGateway);
    }

    @Bean
    ChargeSubscription chargeSubscription(
            final AccountGateway accountGateway,
            final Clock clock,
            final PaymentGateway paymentGateway,
            final PlanGateway planGateway,
            final SubscriptionGateway subscriptionGateway
    ) {
        return new DefaultChargeSubscription(accountGateway, clock, paymentGateway, planGateway, subscriptionGateway);
    }
}
