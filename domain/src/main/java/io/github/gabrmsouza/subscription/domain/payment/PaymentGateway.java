package io.github.gabrmsouza.subscription.domain.payment;

public interface PaymentGateway {
    Transaction processPayment(Payment payment);
}
