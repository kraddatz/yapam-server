package app.yapam.common.service;

import app.yapam.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
public class PermissionEvaluator {

    @Autowired private RequestHelperService requestHelperService;
    @Autowired private SecretRepository secretRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private FileRepository fileRepository;

    public enum SecretAccessPermission {
        READ,
        WRITE
    }

    public Boolean hasAccessToFile(String fileId, SecretAccessPermission permission) {
        var fileDao = fileRepository.findOneById(fileId);

        for (SecretDao secretDao : fileDao.getSecrets()) {
            if (hasAccessToSecret(secretDao.getSecretId(), permission)) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasAccessToSecret(String secretId, SecretAccessPermission permission) {
        var userId = userRepository.findOneByEmail(requestHelperService.getEmail()).getId();
        var secretDao = secretRepository.findFirstBySecretIdOrderByVersionDesc(secretId);

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
}
