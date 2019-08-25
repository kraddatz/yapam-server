package app.yapam.common.service;

import app.yapam.common.repository.SecretDao;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.repository.UserRepository;
import app.yapam.common.repository.UserSecretDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("permissionEvaluator")
public class PermissionEvaluator {

    @Autowired private RequestHelperService requestHelperService;
    @Autowired private SecretRepository secretRepository;
    @Autowired private UserRepository userRepository;

    public enum SecretAccessPermission {
        READ,
        WRITE
    }

    public boolean hasAccessToSecret(String secretId, SecretAccessPermission permission) {
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
