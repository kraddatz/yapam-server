package app.yapam.healthcheck;

import app.yapam.YapamBaseTest;
import app.yapam.config.AppParameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void createHealthCheckResult() {
        var query = (TypedQuery<BigInteger>) mock(TypedQuery.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(BigInteger.ONE);

        var result = healthCheckService.createHealthCheckResult();

        assertTrue(result.getSuccessful());
    }

    @Test
    void createHealthCheckResult_whenDatabaseConnectionFails_thenExpectSuccessfulBeFalse() {
        var query = (TypedQuery<BigInteger>) mock(TypedQuery.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        var result = healthCheckService.createHealthCheckResult();

        assertFalse(result.getSuccessful());
    }
}
