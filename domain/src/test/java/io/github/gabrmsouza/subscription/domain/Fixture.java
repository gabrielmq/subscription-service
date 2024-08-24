package io.github.gabrmsouza.subscription.domain;


import io.github.gabrmsouza.subscription.domain.account.Account;
import io.github.gabrmsouza.subscription.domain.account.AccountId;
import io.github.gabrmsouza.subscription.domain.account.idp.UserId;
import io.github.gabrmsouza.subscription.domain.money.Money;
import io.github.gabrmsouza.subscription.domain.person.Address;
import io.github.gabrmsouza.subscription.domain.person.Document;
import io.github.gabrmsouza.subscription.domain.person.Email;
import io.github.gabrmsouza.subscription.domain.person.Name;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;
import io.github.gabrmsouza.subscription.domain.subscription.Subscription;
import io.github.gabrmsouza.subscription.domain.subscription.SubscriptionId;
import net.datafaker.Faker;

public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static final class Accounts {

        public static Account john() {
            return Account.with(
                    new AccountId("ACC-JOHN"),
                    1,
                    new UserId("USSS-123"),
                    new Email("john@doe.com"),
                    new Name("John", "Doe"),
                    Document.create("12312312312", Document.Cpf.TYPE),
                    new Address("12312123", "123", "", "Brasil")
            );
        }
    }

    public static final class Plans {

        public static Plan plus() {
            return Plan.newPlan(
                    new PlanId(123L),
                    "Plus",
                    FAKER.text().text(100, 500),
                    true,
                    new Money("BRL", 20D)
            );
        }
    }

    public static final class Subscriptions {

        public static Subscription johns() {
            return Subscription.newSubscription(
                    new SubscriptionId("SUB-JOHN-123"),
                    Accounts.john().id(),
                    Plans.plus()
            );
        }
    }
}
