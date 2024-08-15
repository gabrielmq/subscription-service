package io.github.gabrmsouza.subscription.domain;

public interface Identifier<T> extends ValueObject {
    T value();
}
