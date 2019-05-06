package me.raddatz.jwarden.healthcheck.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class HealthCheckResult {

    private Map<String, Object> buildInformation;
    private List<HealthCheck> healthChecks;
}