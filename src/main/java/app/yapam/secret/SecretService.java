package app.yapam.secret;

import app.yapam.common.error.UnknownSecretException;
import app.yapam.common.repository.SecretDao;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.repository.UserRepository;
import app.yapam.common.repository.UserSecretRepository;
import app.yapam.common.service.MappingService;
import app.yapam.file.FileService;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SecretService {

    @Autowired private SecretRepository secretRepository;
    @Autowired private MappingService mappingService;
    @Autowired private UserRepository userRepository;
    @Autowired private UserSecretRepository userSecretRepository;
    @Autowired private FileService fileService;
    @Autowired private TagService tagService;

    private Secret createSecret(Secret secret) {
        secret.setSecretId(UUID.randomUUID().toString());
        secret.setVersion(1);
        var secretDao = secretRepository.save(mappingService.secretToDao(secret));
        userSecretRepository.saveAll(secretDao.getUsers());
        fileService.attachSecretToFiles(secret.getFiles(), secretDao);
        tagService.attachSecretToTags(secret.getTags(), secretDao);
        return secret;
    }

    @PreAuthorize("@permissionEvaluator.registeredUser()")
    SecretResponse createSecret(SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return mappingService.secretToResponse(createSecret(secret));
    }

    @PreAuthorize("@permissionEvaluator.hasAccessToSecret(#secretId, 'READ')")
    void deleteSecret(String secretId) {
        secretRepository.deleteBySecretId(secretId);
    }

    @PreAuthorize("@permissionEvaluator.registeredUser()")
    @PostFilter("@filterEvaluator.filterForKeywords(filterObject, #keywords)")
    List<SimpleSecretResponse> getAllSecrets(String[] keywords) {
        var user = userRepository.findOneById(SecurityContextHolder.getContext().getAuthentication().getName());
        var secretIds = userSecretRepository.findAllByUserId(user.getId()).stream().map(us -> us.getSecret().getSecretId()).distinct().collect(Collectors.toList());
        List<SecretDao> secrets = new ArrayList<>();
        for (String secretId : secretIds) {
            secrets.add(secretRepository.findFirstBySecretIdOrderByVersionDesc(secretId));
        }

        return secrets.stream().map(secret -> mappingService.secretDaoToSimpleResponse(secret)).collect(Collectors.toList());
    }

    @PreAuthorize("@permissionEvaluator.hasAccessToSecret(#secretId, 'WRITE')")
    SecretResponse getSecretById(String secretId, Integer version) {
        SecretDao secret;
        if (version == 0) {
            secret = secretRepository.findFirstBySecretIdOrderByVersionDesc(secretId);
        } else {
            secret = secretRepository.findFirstBySecretIdAndVersion(secretId, version);
        }
        if (Objects.isNull(secret)) {
            throw new UnknownSecretException(secretId);
        }
        return mappingService.secretDaoToResponse(secret);
    }

    @PreAuthorize("@permissionEvaluator.hasAccessToSecret(#secretId, 'WRITE')")
    SecretResponse updateSecret(String secretId, SecretRequest secretRequest) {
        var secret = mappingService.secretFromRequest(secretRequest);
        return mappingService.secretToResponse(updateSecret(secretId, secret));
    }

    private Secret updateSecret(String secretId, Secret secret) {
        secret.setSecretId(secretId);
        var latestSecretVersion = secretRepository.findFirstDistinctVersionBySecretIdOrderByVersionDesc(secretId);
        if (Objects.isNull(latestSecretVersion)) {
            throw new UnknownSecretException(secretId);
        }
        secret.setVersion(latestSecretVersion.getVersion() + 1);
        var secretDao = mappingService.secretToDao(secret);
        secretRepository.save(secretDao);
        userSecretRepository.saveAll(secretDao.getUsers());
        fileService.attachSecretToFiles(secret.getFiles(), secretDao);
        tagService.attachSecretToTags(secret.getTags(), secretDao);
        return secret;
    }
}
