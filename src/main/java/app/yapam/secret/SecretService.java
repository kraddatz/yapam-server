package app.yapam.secret;

import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.repository.SecretRepository;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.repository.SecretTransactions;
import app.yapam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
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
        secret.setVersion(0);
        secret.setCreationDate(LocalDateTime.now());
        secretTransactions.saveSecret(mappingService.secretToDBO(secret));
        return secret;
    }

    SecretResponse createSecret(SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return mappingService.secretToResponse(createSecret(secret));
    }

    private Secret updateSecret(String secretId, Secret secret) {
        secret.setSecretId(secretId);
        var latestSecretVersion = secretRepository.findFirstVersionBySecretIdOrderByVersionDesc(secretId);
        secret.setVersion(latestSecretVersion + 1);
        secret.setCreationDate(LocalDateTime.now());
        secretTransactions.saveSecret(mappingService.secretToDBO(secret));
        return secret;
    }

    SecretResponse updateSecret(String secretId, SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return mappingService.secretToResponse(updateSecret(secretId, secret));
    }

    public Set<SecretResponse> getAllSecrets() {
        return userRepository.findOneByEmail(requestHelperService.getUserName()).getSecrets().stream().map(secret -> mappingService.secretDBOToResponse(secret)).collect(Collectors.toSet());
    }
}
