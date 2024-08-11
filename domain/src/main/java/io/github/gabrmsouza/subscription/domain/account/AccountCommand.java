package io.github.gabrmsouza.subscription.domain.account;

import io.github.gabrmsouza.subscription.domain.person.Address;
import io.github.gabrmsouza.subscription.domain.person.Document;
import io.github.gabrmsouza.subscription.domain.person.Email;
import io.github.gabrmsouza.subscription.domain.person.Name;

public sealed interface AccountCommand {
    record ChangeProfile(Name aName, Address billingAddress) implements AccountCommand {
    }

    record ChangeEmail(Email anEmail) implements AccountCommand {
    }

    record ChangeDocument(Document aDocument) implements AccountCommand {
    }
}
