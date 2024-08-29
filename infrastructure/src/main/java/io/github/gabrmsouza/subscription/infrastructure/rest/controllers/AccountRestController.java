package io.github.gabrmsouza.subscription.infrastructure.rest.controllers;

import io.github.gabrmsouza.subscription.application.account.UpdateBillingInfo;
import io.github.gabrmsouza.subscription.infrastructure.authentication.principal.CodeflixUser;
import io.github.gabrmsouza.subscription.infrastructure.mediator.SignUpMediator;
import io.github.gabrmsouza.subscription.infrastructure.rest.AccountRestApi;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.BillingInfoRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.SignUpRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.BillingInfoResponse;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.SignUpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class AccountRestController implements AccountRestApi {
    private final SignUpMediator signUpMediator;
    private final UpdateBillingInfo updateBillingInfo;

    public AccountRestController(final SignUpMediator signUpMediator, final UpdateBillingInfo updateBillingInfo) {
        this.signUpMediator = Objects.requireNonNull(signUpMediator);
        this.updateBillingInfo = Objects.requireNonNull(updateBillingInfo);
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(final SignUpRequest req) {
        final var res = this.signUpMediator.signUp(req);
        return ResponseEntity.created(URI.create("/accounts/%s".formatted(res.accountId()))).body(res);
    }

    @Override
    public ResponseEntity<BillingInfoResponse> updateBillingInfo(final CodeflixUser principal, final BillingInfoRequest req) {
        record Input(String accountId, String zipcode, String number, String complement, String country)
                implements UpdateBillingInfo.Input {}
        final var input = new Input(principal.accountId(), req.zipcode(), req.number(), req.complement(), req.country());
        final var result = this.updateBillingInfo.execute(input, output -> new BillingInfoResponse(output.accountId().value()));
        return ResponseEntity.accepted().body(result);
    }
}