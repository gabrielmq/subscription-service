package io.github.gabrmsouza.subscription.domain;

@FunctionalInterface
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
