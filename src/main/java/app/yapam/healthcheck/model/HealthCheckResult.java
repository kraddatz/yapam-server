package app.yapam.healthcheck.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@ApiModel
@Getter
@Setter
public class HealthCheckResult {

    private BuildInformation buildInformation;
    private Set<HealthCheck> healthChecks;
    private Boolean successful;
}