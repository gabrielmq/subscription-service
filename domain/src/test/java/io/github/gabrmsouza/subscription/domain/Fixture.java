package io.github.gabrmsouza.subscription.domain;


import io.github.gabrmsouza.subscription.domain.money.Money;
import io.github.gabrmsouza.subscription.domain.plan.Plan;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;
import net.datafaker.Faker;

public final class Fixture {
    private static final Faker FAKER = new Faker();

    private Fixture() {}

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
}
