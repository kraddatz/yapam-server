package me.raddatz.yapam.healthcheck.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HealthCheckResult {

    private Map<String, Object> buildInformation;
    private List<HealthCheck> healthChecks;
    private Map<String, Object> metrics;
    private Boolean successful;
}