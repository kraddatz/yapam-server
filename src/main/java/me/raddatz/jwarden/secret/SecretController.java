package me.raddatz.jwarden.secret;

import me.raddatz.jwarden.secret.model.Secret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {

    @Autowired private SecretService secretService;

    @PostMapping(value = "secrets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Secret createSecret(@RequestBody Secret secret) {
        return secretService.createSecret(secret);
    }
}
