package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PermissionEvaluator.class)
@ActiveProfiles("test")
class PermissionEvaluatorTest extends YapamBaseTest {

    @Autowired private PermissionEvaluator permissionEvaluator;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private SecretRepository secretRepository;
    @MockBean private UserRepository userRepository;

    @Test
    void whenUserHasReadAccess_thenReturnTrue() {
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.READ);

        assertTrue(result);
    }

    @Test
    void whenUserHasNoReadAccess_thenReturnTrue() {
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        secretDao.getUsers().iterator().next().getUser().setId("OTHER-USER-ID");
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.READ);

        assertFalse(result);
    }

    @Test
    void whenUserHasWriteAccess_thenReturnTrue() {
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.WRITE);

        assertTrue(result);
    }

    @Test
    void whenUserHasNoWriteAccess_thenReturnTrue() {
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        secretDao.getUsers().iterator().next().setPrivileged(false);
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.WRITE);

        assertFalse(result);
    }
}
