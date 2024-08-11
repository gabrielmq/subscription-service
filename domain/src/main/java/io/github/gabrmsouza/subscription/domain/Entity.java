package io.github.gabrmsouza.subscription.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> implements AssertionConcern {
    protected final ID id;
    private final List<DomainEvent> events;

    protected Entity(final ID id) {
        this(id, null);
    }

    protected Entity(final ID id, final List<DomainEvent> events) {
        this.id = this.assertArgumentNotNull(id, "'id' should not be null");
        this.events = new ArrayList<>(Objects.requireNonNullElse(events, Collections.emptyList()));
    }

    public ID id() {
        return id;
    }

    public List<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(events);
    }

    public void registerEvent(final DomainEvent event) {
        if (Objects.nonNull(event)) {
            this.events.add(event);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return id().equals(entity.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
