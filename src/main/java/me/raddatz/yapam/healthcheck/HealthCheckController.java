package me.raddatz.yapam.healthcheck;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.raddatz.yapam.healthcheck.model.HealthCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired private HealthCheckService healthCheckService;

    @ApiOperation("Checks if Yapam is running.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "One or more of the required parameters were missing or the used identity is in an invalid state to perform the action."),
            @ApiResponse(code = 401, message = "The authorization credentials are missing or invalid."),
            @ApiResponse(code = 403, message = "Could not fulfill the request since an entity could not be accessed due to security restrictions."),
            @ApiResponse(code = 404, message = "A referenced entity could not be found."),
            @ApiResponse(code = 406, message = "The server cannot deliver any result since the client does not accept one of the supported media types."),
            @ApiResponse(code = 500, message = "A server side error occurred when performing the request.")
    })
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HealthCheckResult healthCheck() {
        return healthCheckService.createHealthCheckResult();
    }
}
