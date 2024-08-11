package io.github.gabrmsouza.subscription.domain.account;

import io.github.gabrmsouza.subscription.domain.Identifier;

public record AccountId(String value) implements Identifier<String> {
    public AccountId {
        this.assertArgumentNotEmpty(value, "'accountId' should not be empty");
    }
}