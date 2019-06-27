package app.yapam.healthcheck;

import app.yapam.YapamBaseTest;
import app.yapam.config.AppParameter;
import app.yapam.secret.repository.SecretRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.CannotCreateTransactionException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HealthCheckService.class)
@ActiveProfiles("test")
class HealthCheckServiceTest extends YapamBaseTest {

    @Autowired private HealthCheckService healthCheckService;
    @MockBean private AppParameter appParameter;
    @MockBean private BuildProperties buildProperties;
    @MockBean private EntityManager entityManager;

    @Test
    void createHealthCheckResult_whenDatabaseConnectionFails_thenExpectSuccessfulBeFalse() {
        var query = (TypedQuery<Integer>) mock(TypedQuery.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        var result = healthCheckService.createHealthCheckResult();

        assertFalse(result.getSuccessful());
    }

    @Test
    void createHealthCheckResult() {
        var query = (TypedQuery<Integer>) mock(TypedQuery.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1);

        var result = healthCheckService.createHealthCheckResult();

        assertTrue(result.getSuccessful());
    }
}