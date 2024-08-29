package io.github.gabrmsouza.subscription;

import io.github.gabrmsouza.subscription.infrastructure.configuration.WebServerConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Inherited
@Tag("integrationTest")
@ActiveProfiles("test-integration")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {
        WebServerConfiguration.class,
        IntegrationTestConfiguration.class
})
@EnableAutoConfiguration(exclude = ElasticsearchRepositoriesAutoConfiguration.class)
public @interface IntegrationTest {
}
