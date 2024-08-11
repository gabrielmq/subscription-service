package io.github.gabrmsouza.subscription.domain.person;

import io.github.gabrmsouza.subscription.domain.ValueObject;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;

public sealed interface Document extends ValueObject {
    String value();
    String type();

    static Document create(final String documentNumber, final String documentType) {
        return Factory.create(documentNumber, documentType);
    }

    final class Factory {
        private Factory() {}
        public static Document create(final String documentNumber, final String documentType) {
            return switch (documentType) {
                case Cpf.TYPE -> new Document.Cpf(documentNumber);
                case Cnpj.TYPE -> new Document.Cnpj(documentNumber);
                default -> throw DomainException.with("Invalid document type");
            };
        }
    }

    record Cpf(String value) implements Document {
        public static final String TYPE = "cpf";
        public Cpf {
            this.assertArgumentNotEmpty(value, "'cpf' should not be empty");
            this.assertArgumentLength(value, 11, "'cpf' is invalid");
        }

        @Override
        public String type() {
            return TYPE;
        }
    }

    record Cnpj(String value) implements Document {
        public static final String TYPE = "cnpj";
        public Cnpj {
            this.assertArgumentNotEmpty(value, "'cnpj' should not be empty");
            this.assertArgumentLength(value, 14, "'cnpj' is invalid");
        }

        @Override
        public String type() {
            return TYPE;
        }
    }
}