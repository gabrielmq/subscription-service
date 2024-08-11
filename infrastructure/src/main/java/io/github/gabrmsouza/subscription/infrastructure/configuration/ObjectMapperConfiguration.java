package io.github.gabrmsouza.subscription.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrmsouza.subscription.infrastructure.json.Json;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

@JsonComponent
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}
