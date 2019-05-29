package app.yapam.healthcheck;

import app.yapam.config.AppParameter;
import app.yapam.secret.repository.SecretRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HealthCheckService.class)
@ActiveProfiles("test")
class HealthCheckServiceTest {

    @Autowired private HealthCheckService healthCheckService;
    @MockBean private AppParameter appParameter;
    @MockBean private BuildProperties buildProperties;
    @MockBean private SecretRepository secretRepository;

    @Test
    void createHealthCheckResult_whenDatabaseConnectionFails_thenExpectSuccessfulBeFalse() {
        when(secretRepository.findAll()).thenThrow(CannotCreateTransactionException.class);

        var result = healthCheckService.createHealthCheckResult();
        assertFalse(result.getSuccessful());
    }

    @Test
    void createHealthCheckResult_whenDatabaseHasInvalidSchema_thenExpectSuccessfulBeFalse() {
        when(secretRepository.findAll()).thenThrow(InvalidDataAccessResourceUsageException.class);

        var result = healthCheckService.createHealthCheckResult();
        assertFalse(result.getSuccessful());
    }

    @Test
    void createHealthCheckResult() {
        when(secretRepository.findAll()).thenReturn(new ArrayList<>());
        assertNotNull(healthCheckService.createHealthCheckResult());
    }
}