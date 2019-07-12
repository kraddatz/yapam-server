package app.yapam.swagger;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class SwaggerController {

    @Autowired private SwaggerService swaggerService;

    @ApiOperation("Gets the swagger documentation as yaml")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "One or more of the required parameters were missing or the used identity is in an invalid state to perform the action."),
            @ApiResponse(code = 401, message = "The authorization credentials are missing or invalid."),
            @ApiResponse(code = 403, message = "Could not fulfill the request since an entity could not be accessed due to security restrictions."),
            @ApiResponse(code = 404, message = "A referenced entity could not be found."),
            @ApiResponse(code = 406, message = "The server cannot deliver any result since the client does not accept one of the supported media types."),
            @ApiResponse(code = 500, message = "A server side error occurred when performing the request.")
    })
    @GetMapping(value = "/documentation/api-docs/yaml", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> yamlSwagger() {
        return ResponseEntity.ok(swaggerService.getSwaggerYamlSpec());
    }

    @GetMapping(value = "/documentation/api-docs/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String json() {
        return "forward:/v2/api-docs";
    }

    @ApiIgnore
    @GetMapping(value = "/documentation/v2/api-docs")
    public String v2ApiDocs() {
        return "forward:/v2/api-docs";
    }

    @ApiIgnore
    @GetMapping(value = "/documentation/swagger-resources/configuration/ui")
    public String resourcesConfigurationUi() {
        return "forward:/swagger-resources/configuration/ui";
    }

    @ApiIgnore
    @GetMapping(value = "/documentation/swagger-resources/configuration/security")
    public String resourcesConfigurationSecurity() {
        return "forward:/swagger-resources/configuration/security";
    }

    @ApiIgnore
    @GetMapping(value = "/documentation/swagger-resources")
    public String resources() {
        return "forward:/swagger-resources";
    }
}
