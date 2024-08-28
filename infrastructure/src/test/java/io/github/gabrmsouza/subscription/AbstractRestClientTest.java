package io.github.gabrmsouza.subscription;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.gabrmsouza.subscription.infrastructure.configuration.WebServerConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

@Tag("integrationTest")
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test-integration")
@SpringBootTest(classes = {
        WebServerConfiguration.class,
        IntegrationTestConfiguration.class
})
public abstract class AbstractRestClientTest {

    @BeforeEach
    void setup() {
        WireMock.reset();
        WireMock.resetAllRequests();
    }
}
