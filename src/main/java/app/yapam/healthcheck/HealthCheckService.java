package app.yapam.healthcheck;

import app.yapam.config.AppParameter;
import app.yapam.healthcheck.model.BuildInformation;
import app.yapam.healthcheck.model.HealthCheck;
import app.yapam.healthcheck.model.HealthCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
class HealthCheckService {

    @Autowired private AppParameter appParameter;
    @Autowired private BuildProperties buildProperties;
    @Autowired private EntityManager entityManager;
    @Autowired ServletContext context;

    HealthCheckResult createHealthCheckResult() {
        HealthCheckResult result = new HealthCheckResult();
        result.setBuildInformation(createBuildInformation());
        result.setHealthChecks(createHealthChecks());
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

    private BuildInformation createBuildInformation() {
        return new BuildInformation(
                appParameter.getBuildTime(),
                System.getProperty("java.version"),
                context.getServerInfo(),
                appParameter.getSpringBootVersion(),
                buildProperties.getVersion()
        );
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


        BigInteger pingDatabase = (BigInteger) entityManager.createNativeQuery("select 1").getSingleResult();
        if (Objects.isNull(pingDatabase)) {
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
