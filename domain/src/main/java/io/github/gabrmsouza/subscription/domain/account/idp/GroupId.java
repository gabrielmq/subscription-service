package io.github.gabrmsouza.subscription.domain.account.idp;

import io.github.gabrmsouza.subscription.domain.Identifier;

public record GroupId(String value) implements Identifier<String> {
    public GroupId {
        this.assertArgumentNotEmpty(value, "'groupId' should not be empty");
    }
}