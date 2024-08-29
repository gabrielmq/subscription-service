package io.github.gabrmsouza.subscription.infrastructure.rest.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BillingInfoRequest(
        @NotBlank @Size(max = 8) String zipcode,
        @NotBlank String number,
        String complement,
        @NotBlank String country
) {
}