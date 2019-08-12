package app.yapam.secret;

import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SecretResponseWrapper;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.secret.repository.SecretDao;
import app.yapam.secret.repository.SecretRepository;
import app.yapam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SecretService {

    @Autowired private SecretRepository secretRepository;
    @Autowired private MappingService mappingService;
    @Autowired private RequestHelperService requestHelperService;
    @Autowired private UserRepository userRepository;

    private Mono<SecretDao> createSecret(Secret secret) {
        secret.setSecretId(UUID.randomUUID().toString());
        secret.setVersion(1);
        secret.setCreationDate(LocalDateTime.now());
        var secretDao = mappingService.secretToDao(secret);
        return secretRepository.save(secretDao);
    }

    Mono<SecretResponse> createSecret(SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return createSecret(secret)
                .map(s -> mappingService.secretDaoToResponse(s));
    }

    void deleteSecret(String secretId) {
        secretRepository.deleteBySecretId(secretId);
    }

    public Flux<SimpleSecretResponse> getAllSecrets() {
        return userRepository.findOneByEmail(requestHelperService.getEmail())
                .flatMapMany(u -> secretRepository.highestSecretsForUser(u.getId()))
                .map(s -> mappingService.secretDaoToSimpleResponse(s));
    }

    Mono<SecretResponse> getSecretById(String secretId, Integer version) {
        Mono<SecretDao> secret;
        if (version == 0) {
            secret = secretRepository.findFirstBySecretIdOrderByVersionDesc(secretId);
        } else {
            secret = secretRepository.findFirstBySecretIdAndVersion(secretId, version);
        }
        return secret.map(s -> mappingService.secretDaoToResponse(s));
    }

    Mono<SecretResponse> updateSecret(String secretId, SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return updateSecret(secretId, secret)
                .map(s -> mappingService.secretToResponse(s));
    }

    private Mono<Secret> updateSecret(String secretId, Secret secret) {
        secret.setSecretId(secretId);
        return secretRepository.findFirstDistinctVersionBySecretIdOrderByVersionDesc(secretId)
                .map(v -> {
                    secret.setVersion(v.getVersion() + 1);
                    secret.setCreationDate(LocalDateTime.now());
                    return secret;
                })
                .flatMap(s -> secretRepository.save(mappingService.secretToDao(s)))
                .map(s -> mappingService.secretFromDao(s));
    }
}
