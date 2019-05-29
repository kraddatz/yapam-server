package app.yapam.healthcheck.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class HealthCheckResult {

    private Map<String, Object> buildInformation;
    private Set<HealthCheck> healthChecks;
    private Map<String, Object> metrics;
    private Boolean successful;
}