package io.github.gabrmsouza.subscription.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN anIn);
}