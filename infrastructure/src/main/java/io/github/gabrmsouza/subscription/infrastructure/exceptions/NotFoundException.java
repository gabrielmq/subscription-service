package io.github.gabrmsouza.subscription.infrastructure.exceptions;

import io.github.gabrmsouza.subscription.domain.exceptions.InternalErrorException;

public class NotFoundException extends InternalErrorException {
    protected NotFoundException(final String aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    public static NotFoundException with(final String aMessage) {
        return new NotFoundException(aMessage, null);
    }
}
