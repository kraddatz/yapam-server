package me.raddatz.yapam.healthcheck;

import me.raddatz.yapam.config.AppParameter;
import me.raddatz.yapam.healthcheck.model.HealthCheck;
import me.raddatz.yapam.healthcheck.model.HealthCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class HealthCheckService {

    @Autowired private AppParameter appParameter;

    HealthCheckResult createHealthCheckResult() {
        return new HealthCheckResult(createBuildInformation(), createHealthChecks());
    }

    private Map<String, Object> createBuildInformation() {
        Map<String, Object> buildInformation = new HashMap<>();
        buildInformation.put("Buildtime", appParameter.getBuildTime());
        buildInformation.put("Java version", appParameter.getJavaVersion());
        buildInformation.put("TomCat version", appParameter.getTomcatVersion());
        buildInformation.put("Spring-Boot version", appParameter.getSpringBootVersion());
        return buildInformation;
    }

    private List<HealthCheck> createHealthChecks() {
        List<HealthCheck> healthChecks = new ArrayList<>();
        healthChecks.add(createApplicationHealthCheck());
        healthChecks.add(createDatabaseHealthCheck());
        return healthChecks;
    }

    private HealthCheck createDatabaseHealthCheck() {
        var healthCheck = new HealthCheck();
        healthCheck.setName("Database");
        healthCheck.setSuccessful(true);
        healthCheck.setAction("Common check if database is up and connected");
        healthCheck.setDate(LocalDateTime.now());
        return healthCheck;
    }

    private HealthCheck createApplicationHealthCheck() {
        var healthCheck = new HealthCheck();
        healthCheck.setName(appParameter.getAppName());
        healthCheck.setSuccessful(true);
        healthCheck.setAction("Common check if " + appParameter.getAppName() + " is up and running");
        healthCheck.setDate(LocalDateTime.now());
        return healthCheck;
    }
}