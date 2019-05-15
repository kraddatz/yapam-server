package me.raddatz.yapam.healthcheck;

import me.raddatz.yapam.config.AppParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HealthCheckService.class)
@ActiveProfiles("test")
class HealthCheckServiceTest {

    @Autowired private HealthCheckService healthCheckService;

    @MockBean private AppParameter appParameter;

    @Test
    void createHealthCheckResult() {
        assertNotNull(healthCheckService.createHealthCheckResult());
    }
}