package app.yapam.secret;

import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SecretResponseWrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecretController {

    @Autowired private SecretService secretService;

    @ApiOperation(value = "Create a new secret")
    @PostMapping(value = "/api/secrets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SecretResponse createSecret(@RequestBody SecretRequest secret) {
        return secretService.createSecret(secret);
    }

    @ApiOperation(value = "Delete a secret by id")
    @DeleteMapping(value = "/api/secrets/{secretId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSecret(@PathVariable(value = "secretId") String secretId) {
        secretService.deleteSecret(secretId);
    }

    @ApiOperation(value = "Get all secrets accessible for a user")
    @GetMapping(value = "/api/secrets", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SecretResponseWrapper getAllSecrets(@RequestParam(value = "keyword", defaultValue = "", required = false) String[] keywords) {
        var secretResponseWrapper = new SecretResponseWrapper();
        secretResponseWrapper.setSecrets(secretService.getAllSecrets(keywords));
        return secretResponseWrapper;
    }

    @ApiOperation(value = "Get a single secret by id")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "secretId", value = "Internal id of the secret", required = true)
    )
    @GetMapping(value = "/api/secrets/{secretId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SecretResponse getSecretById(@PathVariable String secretId,
                                        @RequestParam(value = "version", defaultValue = "0") Integer version) {
        return secretService.getSecretById(secretId, version);
    }

    @ApiOperation(value = "Update a secret by id")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "secretId", value = "Internal id of the secret", required = true)
    )
    @PutMapping(value = "/api/secrets/{secretId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public SecretResponse updateSecret(@PathVariable(value = "secretId") String secretId,
                                       @RequestBody SecretRequest secret) {
        return secretService.updateSecret(secretId, secret);
    }
}
