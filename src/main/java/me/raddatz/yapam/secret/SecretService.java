package me.raddatz.yapam.secret;

import me.raddatz.yapam.common.service.MappingService;
import me.raddatz.yapam.secret.model.Secret;
import me.raddatz.yapam.secret.model.request.SecretRequest;
import me.raddatz.yapam.secret.repository.SecretRepository;
import me.raddatz.yapam.secret.repository.SecretTransactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SecretService {

    @Autowired private SecretRepository secretRepository;
    @Autowired private SecretTransactions secretTransactions;
    @Autowired private MappingService mappingService;

    private Secret createSecret(Secret secret) {
        secret.setSecretId(UUID.randomUUID().toString());
        secret.setVersion(0);
        secret.setCreationDate(LocalDateTime.now());
        secretTransactions.saveSecret(mappingService.secretToDBO(secret));
        return secret;
    }

    Secret createSecret(SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return createSecret(secret);
    }

    private Secret updateSecret(String secretId, Secret secret) {
        secret.setSecretId(secretId);
        var latestSecretVersion = secretRepository.findFirstVersionBySecretIdOrderByVersionDesc(secretId);
        secret.setVersion(latestSecretVersion + 1);
        secret.setCreationDate(LocalDateTime.now());
        secretTransactions.saveSecret(mappingService.secretToDBO(secret));
        return secret;
    }

    Secret updateSecret(String secretId, SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return updateSecret(secretId, secret);
    }
}
