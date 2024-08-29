package io.github.gabrmsouza.subscription;

import io.github.gabrmsouza.subscription.infrastructure.configuration.SecurityConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@WebMvcTest
@Target(TYPE)
@Retention(RUNTIME)
@Tag("integrationTest")
@ActiveProfiles("test-integration")
@Import({ SecurityConfiguration.class, ControllerTestConfiguration.class })
public @interface ControllerTest {
    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
