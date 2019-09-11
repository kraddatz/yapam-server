package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.common.error.UnknownFileException;
import app.yapam.common.error.UnknownSecretException;
import app.yapam.common.repository.FileRepository;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PermissionEvaluator.class)
@ActiveProfiles("test")
class PermissionEvaluatorTest extends YapamBaseTest {

    @Autowired private PermissionEvaluator permissionEvaluator;
    @MockBean private SecretRepository secretRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private FileRepository fileRepository;

    @Test
    void hasAccessToFile_whenUnknownFile_thenThrowUnknownFileException() {
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(null);

        assertThrows(UnknownFileException.class, () -> permissionEvaluator.hasAccessToFile(DEFAULT_FILE_ID, PermissionEvaluator.SecretAccessPermission.READ));
    }

    @Test
    void hasAccessToFile_whenUserHasNoReadAccessToFile_thenReturnFalse() {
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        secretDao.getUsers().get(0).getUser().setId("OTHER-USER-ID");
        var fileDao = createDefaultFileDao();
        fileDao.setSecrets(Collections.singletonList(secretDao));
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(createDefaultFileDao());
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToFile(DEFAULT_FILE_ID, PermissionEvaluator.SecretAccessPermission.READ);

        assertFalse(result);
    }

    @Test
    void hasAccessToFile_whenUserHasNoWriteAccessToFile_thenReturnFalse() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        secretDao.getUsers().get(0).setPrivileged(false);
        var fileDao = createDefaultFileDao();
        fileDao.setSecrets(Collections.singletonList(secretDao));
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(createDefaultFileDao());
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToFile(DEFAULT_FILE_ID, PermissionEvaluator.SecretAccessPermission.WRITE);

        assertFalse(result);
    }

    @Test
    void hasAccessToFile_whenUserHasReadAccessToFile_thenReturnTrue() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        var fileDao = createDefaultFileDao();
        fileDao.setSecrets(Collections.singletonList(secretDao));
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(createDefaultFileDao());
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToFile(DEFAULT_FILE_ID, PermissionEvaluator.SecretAccessPermission.READ);

        assertTrue(result);
    }

    @Test
    void hasAccessToFile_whenUserHasWriteAccessToFile_thenReturnTrue() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        var fileDao = createDefaultFileDao();
        fileDao.setSecrets(Collections.singletonList(secretDao));
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(createDefaultFileDao());
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToFile(DEFAULT_FILE_ID, PermissionEvaluator.SecretAccessPermission.WRITE);

        assertTrue(result);
    }

    @Test
    void hasAccessToSecret_whenUnknownSecret_thenThrowUnknownSecretException() {
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_ID)).thenReturn(null);

        assertThrows(UnknownSecretException.class, () -> permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_ID, PermissionEvaluator.SecretAccessPermission.READ));
    }

    @Test
    void hasAccessToSecret_whenUserHasNoReadAccessToSecret_thenReturnFalse() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        secretDao.getUsers().get(0).getUser().setId("OTHER-USER-ID");
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.READ);

        assertFalse(result);
    }

    @Test
    void hasAccessToSecret_whenUserHasNoWriteAccessToSecret_thenReturnFalse() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        secretDao.getUsers().get(0).setPrivileged(false);
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.WRITE);

        assertFalse(result);
    }

    @Test
    void hasAccessToSecret_whenUserHasReadAccessToSecret_thenReturnTrue() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.READ);

        assertTrue(result);
    }

    @Test
    void hasAccessToSecret_whenUserHasWriteAccessToSecret_thenReturnTrue() {
        mockSecurityContextHolder();
        var userDao = createDefaultUserDao();
        var secretDao = createDefaultSecretDao();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDao);
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);

        var result = permissionEvaluator.hasAccessToSecret(DEFAULT_SECRET_SECRETID, PermissionEvaluator.SecretAccessPermission.WRITE);

        assertTrue(result);
    }

    @Test
    void registeredUser_whenRegisteredUser_thenReturnTrue() {
        mockSecurityContextHolder();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(createDefaultUserDao());

        var result = permissionEvaluator.registeredUser();

        assertTrue(result);
    }

    @Test
    void registeredUser_whenUnregisteredUser_thenReturnFalse() {
        var result = permissionEvaluator.registeredUser();

        assertFalse(result);
    }
}
