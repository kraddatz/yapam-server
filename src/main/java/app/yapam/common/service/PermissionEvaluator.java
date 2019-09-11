package app.yapam.common.service;

import app.yapam.common.error.UnknownFileException;
import app.yapam.common.error.UnknownSecretException;
import app.yapam.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("permissionEvaluator")
public class PermissionEvaluator {

    @Autowired private SecretRepository secretRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private FileRepository fileRepository;

    public Boolean hasAccessToFile(String fileId, SecretAccessPermission permission) {
        var fileDao = fileRepository.findOneById(fileId);
        if (Objects.isNull(fileDao)) {
            throw new UnknownFileException();
        }

        for (SecretDao secretDao : fileDao.getSecrets()) {
            if (hasAccessToSecret(secretDao.getSecretId(), permission)) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasAccessToSecret(String secretId, SecretAccessPermission permission) {
        var userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var secretDao = secretRepository.findFirstBySecretIdOrderByVersionDesc(secretId);
        if (Objects.isNull(secretDao)) {
            throw new UnknownSecretException();
        }

        var readAccess = hasReadAccess(secretDao, userId);
        if (permission == SecretAccessPermission.READ) {
            return readAccess;
        }

        return hasWriteAccess(secretDao);
    }

    private Boolean hasReadAccess(SecretDao secretDao, String userId) {
        return secretDao.getUsers().stream().allMatch(us -> us.getUser().getId().equals(userId));
    }

    private Boolean hasWriteAccess(SecretDao secretDao) {
        return secretDao.getUsers().stream().allMatch(UserSecretDao::getPrivileged);
    }

    public Boolean registeredUser() {
        var userDao = userRepository.findOneById(SecurityContextHolder.getContext().getAuthentication().getName());
        return !Objects.isNull(userDao);
    }

    public enum SecretAccessPermission {
        READ,
        WRITE
    }
}
