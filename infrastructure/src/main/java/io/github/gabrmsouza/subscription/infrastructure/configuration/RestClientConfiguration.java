package io.github.gabrmsouza.subscription.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.gabrmsouza.subscription.infrastructure.configuration.annontations.Keycloak;
import io.github.gabrmsouza.subscription.infrastructure.configuration.properties.RestClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class RestClientConfiguration {
    @Bean
    @Keycloak
    @ConfigurationProperties("rest-client.keycloak")
    RestClientProperties keycloakRestClientProperties() {
        return new RestClientProperties();
    }

    @Bean
    @Keycloak
    RestClient keycloakHttpClient(@Keycloak final RestClientProperties properties, final ObjectMapper mapper) {
        return restClient(properties, mapper);
    }

    private RestClient restClient(final RestClientProperties properties, final ObjectMapper mapper) {
        final var factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(properties.readTimeout());
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(factory)
                .messageConverters(converters -> {
                    converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
                    converters.add(jsonConverter(mapper));
                    converters.add(new FormHttpMessageConverter());
                })
                .build();
    }

    private MappingJackson2HttpMessageConverter jsonConverter(final ObjectMapper mapper) {
        final var converter = new MappingJackson2HttpMessageConverter(mapper);
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON));
        return converter;
    }
}
