package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MappingService.class)
@ActiveProfiles("test")
class MappingServiceTest extends YapamBaseTest {

    @Autowired private MappingService mappingService;
    @MockBean private UserRepository userRepository;

    @Test
    void secretFromDBO_whenSecretDBOHasNoUser() {
        var secretDBO = createDefaultSecretDBO();
        secretDBO.setUser(null);

        var result = mappingService.secretFromDBO(secretDBO);

        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data faileed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretid failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertThrows(NullPointerException.class, () -> result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretFromDBO_whenSecretDBOHasUser() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretFromDBO(secretDBO);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretid failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
    }

    @Test
    void secretToDBO_whenSecretHasNoUser() {
        var secret = createDefaultSecret();
        secret.setUser(null);

        var result = mappingService.secretToDBO(secret);

        assertThrows(NullPointerException.class, () -> result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretid failed");
        assertNull(result.getId(), "id failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretToDBO_whenSecretHasUser() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToDBO(secret);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "username failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertNull(result.getId(), "id failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretid failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
    }

    @Test
    void secretFromRequest_whenSecretRequestHasNoUserId() {
        var secretRequest = createDefaultSecretRequest();
        secretRequest.setUserId(null);

        var result = mappingService.secretFromRequest(secretRequest);

        assertThrows(NullPointerException.class, () -> result.getUser().getName(), "user failed");
        assertNull(result.getVersion(), "version failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertNull(result.getSecretId(), "secretid failed");
        assertNull(result.getCreationDate(), "creationdate failed");
    }

    @Test
    void secretFromRequest_whenSecretRequestHasUserId() {
        var secretRequest = createDefaultSecretRequest();
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);

        var result = mappingService.secretFromRequest(secretRequest);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "user failed");
        assertNull(result.getCreationDate(), "creationdate failed");
        assertNull(result.getSecretId(), "secretid failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertNull(result.getVersion());
    }

    @Test
    void secretToResponse_whenSecretHasNoUser() {
        var secret = createDefaultSecret();
        secret.setUser(null);

        var result = mappingService.secretToResponse(secret);

        assertThrows(NullPointerException.class, () -> result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretid failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretToResponse_whenSecretHasUser() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToResponse(secret);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "name failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretid failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretDBOToResponse() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretDBOToResponse(secretDBO);

        assertNotNull(result);
    }

    @Test
    void userFromDBO() {
        var userDBO = createDefaultUserDBO();

        var result = mappingService.userFromDBO(userDBO);

        assertEquals(DEFAULT_USER_NAME, result.getName(), "name failed");
        assertEquals(DEFAULT_USER_CULTURE, result.getCulture(), "culture failed");
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail(), "email failed");
        assertEquals(DEFAULT_USER_EMAIL_TOKEN, result.getEmailToken(), "token failed");
        assertEquals(DEFAULT_USER_ID, result.getId(), "id failed");
        assertEquals(DEFAULT_USER_PASSWORD_HASH, result.getMasterPasswordHash(), "hash failed");
        assertEquals(DEFAULT_USER_PASSWORD_HINT, result.getMasterPasswordHint(), "hint failed");
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey(), "publickey failed");
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAULT_USER_EMAIL_VERIFIED, result.getEmailVerified(), "verified failed");
    }

    @Test
    void userFromRequest() {
        var userRequest = createDefaultUserRequest();

        var result = mappingService.userFromRequest(userRequest);

        assertEquals(DEFAULT_USER_NAME, result.getName(), "name failed");
        assertEquals(DEFAULT_USER_CULTURE, result.getCulture(), "culture failed");
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail(), "email failed");
        assertNull(result.getEmailToken(), "token failed");
        assertNull(result.getId(), "id failed");
        assertEquals(DEFAULT_USER_PASSWORD_HASH, result.getMasterPasswordHash(), "hash failed");
        assertEquals(DEFAULT_USER_PASSWORD_HINT, result.getMasterPasswordHint(), "hint failed");
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey(), "publickey failed");
        assertNull(result.getCreationDate(), "creationdate failed");
        assertNull(result.getEmailVerified(), "verified failed");
    }

    @Test
    void userToDBO() {
        var user = createDefaultUser();

        var result = mappingService.userToDBO(user);

        assertEquals(DEFAULT_USER_NAME, result.getName(), "name failed");
        assertEquals(DEFAULT_USER_CULTURE, result.getCulture(), "culture failed");
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail(), "email failed");
        assertEquals(DEFAULT_USER_EMAIL_TOKEN, result.getEmailToken(), "token failed");
        assertEquals(DEFAULT_USER_ID, result.getId(), "id failed");
        assertEquals(DEFAULT_USER_PASSWORD_HASH, result.getMasterPasswordHash(), "hash failed");
        assertEquals(DEFAULT_USER_PASSWORD_HINT, result.getMasterPasswordHint(), "hint failed");
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey(), "publickey failed");
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate(), "creationdate failed");
        assertEquals(DEFAULT_USER_EMAIL_VERIFIED, result.getEmailVerified(), "verified failed");
    }

    @Test
    void userToResponse() {
        var user = createDefaultUser();

        var result = mappingService.userToResponse(user);

        assertEquals(DEFAULT_USER_NAME, result.getName());
        assertEquals(DEFAULT_USER_CULTURE, result.getCulture());
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertEquals(DEFAULT_USER_ID, result.getId());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_USER_EMAIL_VERIFIED, result.getEmailVerified());
    }

    @Test
    void userDBOToSimpleResponse() {
        var userDBO = createDefaultUserDBO();

        var result = mappingService.userDBOToSimpleResponse(userDBO);

        assertNotNull(result);
    }

    @Test
    void userDBOToResponse() {
        var userDBO = createDefaultUserDBO();

        var result = mappingService.userDBOToResponse(userDBO);

        assertNotNull(result);
    }

    @Test
    void copyUserRequestToDBO() {
        var userRequest = createDefaultUserRequest();
        userRequest.setName("newName");
        userRequest.setEmail("newemail@email.com");
        userRequest.setPublicKey("newPublicKey");
        userRequest.setMasterPasswordHint("passwordisnewpassword");
        userRequest.setMasterPasswordHash("$2a$10$N2ksTrW68GDLA2PEdRuJm.zUHCWi9Mi2H6fYbXBjhk3AAa.29x1Sq");
        userRequest.setCulture("en-EN");
        var userDBO = createDefaultUserDBO();

        userDBO = mappingService.copyUserRequestToDBO(userRequest, userDBO);

        assertEquals("newName", userDBO.getName());
        assertEquals("newemail@email.com", userDBO.getEmail());
        assertEquals("newPublicKey", userDBO.getPublicKey());
        assertEquals("passwordisnewpassword", userDBO.getMasterPasswordHint());
        assertEquals("$2a$10$N2ksTrW68GDLA2PEdRuJm.zUHCWi9Mi2H6fYbXBjhk3AAa.29x1Sq", userDBO.getMasterPasswordHash());
    }

    @Test
    void copyUserToNullDBO() {
        var userRequest = createDefaultUserRequest();
        var userDBO = new UserDBO();

        userDBO = mappingService.copyUserRequestToDBO(userRequest, userDBO);

        assertEquals(DEFAULT_USER_NAME, userDBO.getName());
        assertEquals(DEFAULT_USER_EMAIL, userDBO.getEmail());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, userDBO.getPublicKey());
        assertEquals(DEFAULT_USER_PASSWORD_HINT, userDBO.getMasterPasswordHint());
        assertEquals(DEFAULT_USER_PASSWORD_HASH, userDBO.getMasterPasswordHash());
    }
}