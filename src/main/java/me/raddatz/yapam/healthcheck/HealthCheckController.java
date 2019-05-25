package me.raddatz.yapam.healthcheck;

import me.raddatz.yapam.healthcheck.model.HealthCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired private HealthCheckService healthCheckService;

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HealthCheckResult healthCheck() {
        return healthCheckService.createHealthCheckResult();
    }

    @GetMapping(value = "/health")
    @ResponseStatus(HttpStatus.OK)
    public void isHealthy() {
        // Do nothing, just return Status 200 (OK)
    }
}
