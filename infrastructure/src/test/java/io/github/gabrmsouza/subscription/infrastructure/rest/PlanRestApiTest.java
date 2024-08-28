package io.github.gabrmsouza.subscription.infrastructure.rest;

import io.github.gabrmsouza.subscription.ControllerTest;
import io.github.gabrmsouza.subscription.application.Presenter;
import io.github.gabrmsouza.subscription.application.plan.ChangePlan;
import io.github.gabrmsouza.subscription.application.plan.CreatePlan;
import io.github.gabrmsouza.subscription.domain.plan.PlanId;
import io.github.gabrmsouza.subscription.infrastructure.rest.controllers.PlanRestController;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.ChangePlanResponse;
import io.github.gabrmsouza.subscription.infrastructure.rest.models.response.CreatePlanResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static io.github.gabrmsouza.subscription.ApiTest.admin;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = PlanRestController.class)
public class PlanRestApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreatePlan createPlan;

    @MockBean
    private ChangePlan changePlan;

    @Captor
    private ArgumentCaptor<CreatePlan.Input> createPlanInputCaptor;

    @Captor
    private ArgumentCaptor<ChangePlan.Input> changePlanInputCaptor;

    @Test
    public void givenValidInput_whenCreateSuccessfully_shouldReturnPlanId() throws Exception {
        // given
        var expectedName = "Plus";
        var expectedDescription = "O melhor plano";
        var expectedPrice = 20D;
        var expectedCurrency = "BRL";
        var expectedActive = true;
        var expectedPlanId = new PlanId(123L);

        when(createPlan.execute(any(), any())).thenAnswer(call -> {
            Presenter<CreatePlan.Output, CreatePlanResponse> p = call.getArgument(1);
            return p.apply(new PlanTestOutput(expectedPlanId));
        });

        var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "currency": "%s",
                    "active": %s
                }
                """.formatted(expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive);

        // when
        var aRequest = post("/plans")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin());

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/plans/" + expectedPlanId.value()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.plan_id").value(equalTo(expectedPlanId.value()), Long.class));

        verify(createPlan, times(1)).execute(createPlanInputCaptor.capture(), any());

        var actualRequest = createPlanInputCaptor.getValue();

        assertEquals(expectedName, actualRequest.name());
        assertEquals(expectedDescription, actualRequest.description());
        assertEquals(expectedPrice, actualRequest.price());
        assertEquals(expectedCurrency, actualRequest.currency());
        assertEquals(expectedActive, actualRequest.active());
    }

    @Test
    public void givenEmptyFirstname_shouldReturnError() throws Exception {
        // given
        var expectedErrorProperty = "name";
        var expectedErrorMessage = "must not be blank";
        var expectedName = " ";
        var expectedDescription = "O melhor plano";
        var expectedPrice = 20D;
        var expectedCurrency = "BRL";
        var expectedActive = true;

        var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "currency": "%s",
                    "active": %s
                }
                """.formatted(expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive);

        // when
        var aRequest = post("/plans")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin());

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].property", equalTo(expectedErrorProperty)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createPlan, times(0)).execute(any(), any());
    }

    @Test
    public void givenValidInput_whenChangeSuccessfully_shouldReturnPlanId() throws Exception {
        // given
        var expectedId = 123L;
        var expectedName = "Plus";
        var expectedDescription = "O melhor plano";
        var expectedPrice = 20D;
        var expectedCurrency = "BRL";
        var expectedActive = true;
        var expectedPlanId = new PlanId(123L);

        when(changePlan.execute(any(), any())).thenAnswer(call -> {
            Presenter<ChangePlan.Output, ChangePlanResponse> p = call.getArgument(1);
            return p.apply(new PlanTestOutput(expectedPlanId));
        });

        var json = """
                {
                    "plan_id": %s,
                    "name": "%s",
                    "description": "%s",
                    "price": %s,
                    "currency": "%s",
                    "active": %s
                }
                """.formatted(expectedId, expectedName, expectedDescription, expectedPrice, expectedCurrency, expectedActive);

        // when
        var aRequest = put("/plans/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(admin());

        var aResponse = this.mvc.perform(aRequest);

        // then
        aResponse
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.plan_id").value(equalTo(expectedPlanId.value()), Long.class));

        verify(changePlan).execute(changePlanInputCaptor.capture(), any());

        var actualRequest = changePlanInputCaptor.getValue();

        assertEquals(expectedId, actualRequest.planId());
        assertEquals(expectedName, actualRequest.name());
        assertEquals(expectedDescription, actualRequest.description());
        assertEquals(expectedPrice, actualRequest.price());
        assertEquals(expectedCurrency, actualRequest.currency());
        assertEquals(expectedActive, actualRequest.active());
    }

    record PlanTestOutput(PlanId planId) implements CreatePlan.Output, ChangePlan.Output {}

}