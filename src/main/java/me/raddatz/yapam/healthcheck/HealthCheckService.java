package me.raddatz.yapam.healthcheck;

import me.raddatz.yapam.config.AppParameter;
import me.raddatz.yapam.healthcheck.model.HealthCheck;
import me.raddatz.yapam.healthcheck.model.HealthCheckResult;
import me.raddatz.yapam.secret.repository.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
class HealthCheckService {

    @Autowired private AppParameter appParameter;
    @Autowired private SecretRepository secretRepository;
    @Autowired private BuildProperties buildProperties;

    HealthCheckResult createHealthCheckResult() {
        HealthCheckResult result = new HealthCheckResult();
        result.setBuildInformation(createBuildInformation());
        result.setHealthChecks(createHealthChecks());
        result.setMetrics(createMetrics());
        result.setSuccessful(getSuccessful(result));
        return result;
    }

    private Boolean getSuccessful(HealthCheckResult healthCheckResult) {
        boolean successful = true;
        for (HealthCheck healthCheck : healthCheckResult.getHealthChecks()) {
            if (!healthCheck.getSuccessful()) {
                successful = false;
                break;
            }
        }
        return successful;
    }

    private Map<String, Object> createMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("Uptime", Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime()));
        return metrics;
    }

    private Map<String, Object> createBuildInformation() {
        Map<String, Object> buildInformation = new HashMap<>();
        buildInformation.put("Buildtime", appParameter.getBuildTime());
        buildInformation.put("Java version", System.getProperty("java.version"));
        buildInformation.put("Built-in TomCat version", appParameter.getTomcatVersion());
        buildInformation.put("Spring-Boot version", appParameter.getSpringBootVersion());
        buildInformation.put("Application version", buildProperties.getVersion());
        return buildInformation;
    }

    private Set<HealthCheck> createHealthChecks() {
        Set<HealthCheck> healthChecks = new HashSet<>();
        healthChecks.add(createApplicationHealthCheck());
        healthChecks.add(createDatabaseHealthCheck());
        return healthChecks;
    }

    private HealthCheck createApplicationHealthCheck() {
        return HealthCheck.builder()
                .name(appParameter.getAppName())
                .successful(true)
                .action("Common check if " + appParameter.getAppName() + " is up and running")
                .date(LocalDateTime.now())
                .build();
    }

    private HealthCheck createDatabaseHealthCheck() {
        boolean successful = true;
        String error = null;
        try {
            secretRepository.findAll();
        } catch (InvalidDataAccessResourceUsageException e1) {
            successful = false;
            error = "Invalid schema on database";
        } catch (CannotCreateTransactionException e2) {
            successful = false;
            error = "Database not connected";
        }

        return HealthCheck.builder()
                .name("Database")
                .successful(successful)
                .action("Simple request against database to check if is up and running")
                .date(LocalDateTime.now())
                .error(error)
                .build();
    }
}
