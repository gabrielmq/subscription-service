package io.github.gabrmsouza.subscription.domain.person;

import io.github.gabrmsouza.subscription.domain.ValueObject;

public record Name(String firstname, String lastname) implements ValueObject {

    public Name {
        this.assertArgumentNotEmpty(firstname, "'firstname' should not be empty");
        this.assertArgumentNotEmpty(lastname, "'lastname' should not be empty");
    }

    public String fullname() {
        return this.firstname().concat(" ").concat(lastname());
    }
}
