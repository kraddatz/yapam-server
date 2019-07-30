package app.yapam.secret;

import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SecretResponseWrapper;
import app.yapam.secret.repository.SecretDBO;
import app.yapam.secret.repository.SecretRepository;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.repository.SecretTransactions;
import app.yapam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SecretService {

    @Autowired private SecretRepository secretRepository;
    @Autowired private SecretTransactions secretTransactions;
    @Autowired private MappingService mappingService;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private UserRepository userRepository;

    private Secret createSecret(Secret secret) {
        secret.setSecretId(UUID.randomUUID().toString());
        secret.setVersion(1);
        secret.setCreationDate(LocalDateTime.now());
        secretTransactions.saveSecret(mappingService.secretToDBO(secret));
        return secret;
    }

    SecretResponse createSecret(SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return mappingService.secretToResponse(createSecret(secret));
    }

    void deleteSecret(String secretId) {
        secretRepository.deleteBySecretId(secretId);
    }

    SecretResponse getSecretById(String secretId, Integer version) {
        SecretDBO secret;
        if (version == 0) {
            secret = secretRepository.findFirstBySecretIdOrderByVersionDesc(secretId);
        } else {
            secret = secretRepository.findFirstBySecretIdAndVersion(secretId, version);
        }
        return mappingService.secretDBOToResponse(secret);
    }

    private Secret updateSecret(String secretId, Secret secret) {
        secret.setSecretId(secretId);
        var latestSecretVersion = secretRepository.findFirstDistinctVersionBySecretIdOrderByVersionDesc(secretId);
        secret.setVersion(latestSecretVersion.getVersion() + 1);
        secret.setCreationDate(LocalDateTime.now());
        secretTransactions.saveSecret(mappingService.secretToDBO(secret));
        return secret;
    }

    SecretResponse updateSecret(String secretId, SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return mappingService.secretToResponse(updateSecret(secretId, secret));
    }

    public SecretResponseWrapper getAllSecrets() {
        var user = userRepository.findOneByEmail(requestHelperService.getUserName());
        var secrets = secretRepository.highestSecretsForUser(user.getId());

        var simpleSecretResponse = secrets.stream().map(secret -> mappingService.secretDBOToSimpleResponse(secret)).collect(Collectors.toSet());
        var secretResponseWrapper = new SecretResponseWrapper();
        secretResponseWrapper.setSecrets(simpleSecretResponse);

        return secretResponseWrapper;
    }
}
