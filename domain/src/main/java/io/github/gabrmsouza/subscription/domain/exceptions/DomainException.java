package io.github.gabrmsouza.subscription.domain.exceptions;

import io.github.gabrmsouza.subscription.domain.AggregateRoot;
import io.github.gabrmsouza.subscription.domain.Identifier;
import io.github.gabrmsouza.subscription.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {
    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public static DomainException with(final String aMessage) {
        return new DomainException(aMessage, List.of(new Error("", aMessage)));
    }

    public static DomainException notFound(Class<? extends AggregateRoot<?>> aggClass, Identifier id) {
        return DomainException.with("%s with id %s was not found".formatted(aggClass.getCanonicalName(), id.value()));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
