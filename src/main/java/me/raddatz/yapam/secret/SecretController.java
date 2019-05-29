package me.raddatz.yapam.secret;

import me.raddatz.yapam.common.annotation.verifiedemail.VerifiedEmail;
import me.raddatz.yapam.secret.model.request.SecretRequest;
import me.raddatz.yapam.secret.model.response.SecretResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class SecretController {

    @Autowired private SecretService secretService;

    @GetMapping(value = "/api/secrets")
    @ResponseBody
    @VerifiedEmail
    public Set<SecretResponse> getAllSecrets() {
        return secretService.getAllSecrets();
    }

    @PostMapping(value = "/api/secrets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @VerifiedEmail
    public SecretResponse createSecret(@RequestBody SecretRequest secret) {
        return secretService.createSecret(secret);
    }

    @PutMapping(value = "/api/secrets/{secretId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @VerifiedEmail
    public SecretResponse updateSecret(@PathVariable(value = "secretId") String secretId,
                               @RequestBody SecretRequest secret) {
        return secretService.updateSecret(secretId, secret);
    }
}
