package app.yapam.secret;

import app.yapam.common.annotation.verifiedemail.VerifiedEmail;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class SecretController {

    @Autowired private SecretService secretService;

    @PostMapping(value = "/api/secrets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @VerifiedEmail
    public SecretResponse createSecret(@RequestBody SecretRequest secret) {
        return secretService.createSecret(secret);
    }

    @GetMapping(value = "/api/secrets")
    @ResponseBody
    @VerifiedEmail
    public Set<SecretResponse> getAllSecrets() {
        return secretService.getAllSecrets();
    }

    @GetMapping(value = "/api/secrets/{secretId}")
    @ResponseBody
    @VerifiedEmail
    public SecretResponse getSecretById(@PathVariable String secretId, @RequestParam("version") Integer version) {
        return secretService.getSecretById(secretId, version);
    }

    @PutMapping(value = "/api/secrets/{secretId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @VerifiedEmail
    public SecretResponse updateSecret(@PathVariable(value = "secretId") String secretId,
                                       @RequestBody SecretRequest secret) {
        return secretService.updateSecret(secretId, secret);
    }

    @DeleteMapping(value = "/api/secrets/{secretId}")
    @VerifiedEmail
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSecrete(@PathVariable(value = "secretId") String secretId) {
        secretService.deleteSecret(secretId);
    }
}
