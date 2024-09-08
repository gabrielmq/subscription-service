package io.github.gabrmsouza.subscription.infrastructure.configuration;

import io.github.gabrmsouza.subscription.infrastructure.jdbc.DatabaseClient;
import io.github.gabrmsouza.subscription.infrastructure.jdbc.JdbcClientAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration(proxyBeanMethods = false)
public class JdbcConfiguration {
    @Bean
    DatabaseClient databaseClient(JdbcClient jdbcClient) {
        return new JdbcClientAdapter(jdbcClient);
    }
}
