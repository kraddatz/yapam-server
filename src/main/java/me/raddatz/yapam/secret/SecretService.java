package me.raddatz.yapam.secret;

import me.raddatz.yapam.common.error.UserNotFoundException;
import me.raddatz.yapam.common.service.RequestHelperService;
import me.raddatz.yapam.secret.model.Secret;
import me.raddatz.yapam.secret.model.SecretRequest;
import me.raddatz.yapam.secret.repository.SecretRepository;
import me.raddatz.yapam.secret.repository.SecretTransaction;
import me.raddatz.yapam.user.model.User;
import me.raddatz.yapam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class SecretService {

    @Autowired private SecretRepository secretRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private SecretTransaction secretTransaction;

    private User findUserForSecret() {
        var user = userRepository.findOneByEmail(requestHelperService.getUserName());
        if (Objects.isNull(user)) {
            throw new UserNotFoundException();
        }
        return new User(user);
    }

    private Secret createSecret(Secret secret) {
        secret.setSecretId(UUID.randomUUID().toString());
        secret.setVersion(0);
        secret.setCreationDate(LocalDateTime.now());
        secret.setUser(findUserForSecret());
        secretTransaction.saveSecret(secret);
        return secret;
    }

    Secret createSecret(SecretRequest secretRequest) {
        var secret = new Secret(secretRequest);
        return createSecret(secret);
    }

    private Secret updateSecret(String secretId, Secret secret) {
        secret.setSecretId(secretId);
        var latestSecretVersion = secretRepository.findFirstVersionBySecretIdOrderByVersionDesc(secretId);
        secret.setVersion(latestSecretVersion + 1);
        secret.setUser(findUserForSecret());
        secretTransaction.saveSecret(secret);
        return secret;
    }

    Secret updateSecret(String secretId, SecretRequest secretRequest) {
        var secret = new Secret(secretRequest);
        return updateSecret(secretId, secret);
    }
}
