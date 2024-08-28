package io.github.gabrmsouza.subscription.infrastructure.authentication.principal;

public interface User {
    String name();
    String idpUserId();
    String accountId();
}