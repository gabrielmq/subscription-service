package io.github.gabrmsouza.subscription.domain.account.iam;

import io.github.gabrmsouza.subscription.domain.Identifier;

public record UserId(String value) implements Identifier<String> {
    public UserId {
        this.assertArgumentNotEmpty(value, "'userId' should not be empty");
    }
}