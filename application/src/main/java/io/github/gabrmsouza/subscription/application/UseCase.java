package io.github.gabrmsouza.subscription.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN anIn);
}