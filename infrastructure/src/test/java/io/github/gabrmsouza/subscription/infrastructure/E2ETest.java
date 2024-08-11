package io.github.gabrmsouza.subscription.infrastructure;

import io.github.gabrmsouza.subscription.infrastructure.configuration.WebServerConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Target(TYPE)
@Tag("e2eTest")
@Retention(RUNTIME)
@AutoConfigureMockMvc
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = WebServerConfiguration.class)
public @interface E2ETest {
}
