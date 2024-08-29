package io.github.gabrmsouza.subscription.infrastructure.gateway.client;

import io.github.gabrmsouza.subscription.domain.payment.Payment;
import io.github.gabrmsouza.subscription.domain.payment.PaymentGateway;
import io.github.gabrmsouza.subscription.domain.payment.Transaction;
import io.github.gabrmsouza.subscription.domain.utils.IDUtils;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class PagarmeClient implements PaymentGateway {
    @Override
    public Transaction processPayment(Payment payment) {
        if (LocalTime.now().getMinute() %2 == 0) {
            return Transaction.success(IDUtils.uuid());
        }
        return Transaction.failure(IDUtils.uuid(), "Not enough funds");
    }
}
