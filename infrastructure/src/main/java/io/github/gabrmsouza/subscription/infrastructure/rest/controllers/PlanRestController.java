package io.github.gabrmsouza.subscription.infrastructure.rest.controllers;

import io.github.gabrmsouza.subscription.application.plan.ChangePlan;
import io.github.gabrmsouza.subscription.application.plan.CreatePlan;
import io.github.gabrmsouza.subscription.domain.exceptions.DomainException;
import io.github.gabrmsouza.subscription.infrastructure.rest.PlanRestApi;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.ChangePlanRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.request.CreatePlanRequest;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.ChangePlanResponse;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.CreatePlanResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class PlanRestController implements PlanRestApi {
    private final CreatePlan createPlan;
    private final ChangePlan changePlan;

    public PlanRestController(final CreatePlan createPlan, final ChangePlan changePlan) {
        this.createPlan = Objects.requireNonNull(createPlan);
        this.changePlan = Objects.requireNonNull(changePlan);
    }

    @Override
    public ResponseEntity<CreatePlanResponse> createPlan(final CreatePlanRequest req) {
        final var res = this.createPlan.execute(req, CreatePlanResponse::new);
        return ResponseEntity.created(URI.create("/plans/" + res.planId())).body(res);
    }

    @Override
    public ResponseEntity<ChangePlanResponse> changePlan(final Long planId, final ChangePlanRequest req) {
        if (!Objects.equals(req.planId(), planId)) {
            throw DomainException.with("Plan identifier doesn't matches");
        }
        return ResponseEntity.ok().body(this.changePlan.execute(req, ChangePlanResponse::new));
    }
}