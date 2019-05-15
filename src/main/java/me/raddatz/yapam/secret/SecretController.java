package me.raddatz.yapam.secret;

import me.raddatz.yapam.common.annotation.verifiedemail.VerifiedEmail;
import me.raddatz.yapam.secret.model.Secret;
import me.raddatz.yapam.secret.model.SecretRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecretController {

    @Autowired private SecretService secretService;

    @PostMapping(value = "secrets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @VerifiedEmail
    public Secret createSecret(@RequestBody SecretRequest secret) {
        return secretService.createSecret(secret);
    }

    @PostMapping(value = "secrets/{secretId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @VerifiedEmail
    public Secret updateSecret(@PathVariable(value = "secretId") String secretId,
                               @RequestBody SecretRequest secret) {
        return secretService.updateSecret(secretId, secret);
    }
}
