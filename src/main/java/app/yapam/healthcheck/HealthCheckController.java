package app.yapam.healthcheck;

import app.yapam.healthcheck.model.HealthCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired private HealthCheckService healthCheckService;

    @GetMapping(value = "/api/status", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void healthCheck() {
        // Do nothing, just return Status 200 (OK)
    }

    @GetMapping(value = "/api/health")
    @ResponseStatus(HttpStatus.OK)
    public HealthCheckResult isHealthy() {
        return healthCheckService.createHealthCheckResult();

    }
}
