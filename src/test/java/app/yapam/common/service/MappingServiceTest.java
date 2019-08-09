package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MappingService.class)
@ActiveProfiles("test")
class MappingServiceTest extends YapamBaseTest {

    @Autowired private MappingService mappingService;
    @MockBean private UserRepository userRepository;
    @MockBean private RequestHelperService requestHelperService;

    @Test
    void secretFromDBO_whenSecretDBOHasNoUser() {
        var secretDBO = createDefaultSecretDBO();
        secretDBO.setUser(null);

        var result = mappingService.secretFromDao(secretDBO);

        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data faileed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationDate failed");
        assertThrows(NullPointerException.class, () -> result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretFromDBO_whenSecretDBOHasUser() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretFromDao(secretDBO);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationDate failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
    }

    @Test
    void secretToDBO_whenSecretHasNoUser() {
        var secret = createDefaultSecret();
        secret.setUser(null);

        var result = mappingService.secretToDao(secret);

        assertThrows(NullPointerException.class, () -> result.getUser().getName(), "user failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
        assertNull(result.getId(), "id failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationDate failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretToDBO_whenSecretHasUser() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToDao(secret);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "username failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationDate failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertNull(result.getId(), "id failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
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
        assertNull(result.getSecretId(), "secretId failed");
        assertNull(result.getCreationDate(), "creationDate failed");
    }

    @Test
    void secretFromRequest_whenSecretRequestHasUserId() {
        var secretRequest = createDefaultSecretRequest();
        var userDBO = createDefaultUserDao();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);

        var result = mappingService.secretFromRequest(secretRequest);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "user failed");
        assertNull(result.getCreationDate(), "creationDate failed");
        assertNull(result.getSecretId(), "secretId failed");
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
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationDate failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretToResponse_whenSecretHasUser() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToResponse(secret);

        assertEquals(DEFAULT_USER_NAME, result.getUser().getName(), "name failed");
        assertEquals(DEFAUlT_SECRET_DATA, result.getData(), "data failed");
        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
        assertEquals(DEFAUlT_SECRET_CREATION_DATE, result.getCreationDate(), "creationDate failed");
        assertEquals(DEFAUlT_SECRET_TYPE, result.getType(), "type failed");
        assertEquals(DEFAUlT_SECRET_VERSION, result.getVersion(), "version failed");
    }

    @Test
    void secretDBOToResponse() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretDaoToResponse(secretDBO);

        assertNotNull(result);
    }

    @Test
    void secretToSimpleResponse() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToSimpleResponse(secret);

        assertEquals(DEFAUlT_SECRET_SECRETID, result.getSecretId(), "secretId failed");
        assertEquals(DEFAUlT_SECRET_TITLE, result.getTitle(), "title failed");
    }

    @Test
    void secretDBOToSimpleResponse() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretDaoToSimpleResponse(secretDBO);

        assertNotNull(result);
    }

    @Test
    void userFromDBO() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userFromDao(userDBO);

        assertEquals(DEFAULT_USER_NAME, result.getName(), "name failed");
        assertEquals(DEFAULT_USER_LOCALE, result.getLocale(), "locale failed");
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail(), "email failed");
        assertEquals(DEFAULT_USER_ID, result.getId(), "id failed");
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey(), "publickey failed");
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate(), "creationdate failed");
    }

    @Test
    void userFromRequest() {
        var userRequest = createDefaultUserRequest();
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);

        var result = mappingService.userFromRequest(userRequest);

        assertEquals(DEFAULT_USER_NAME, result.getName(), "name failed");
        assertEquals(DEFAULT_USER_LOCALE, result.getLocale(), "culture failed");
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail(), "email failed");
        assertNull(result.getId(), "id failed");
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey(), "publickey failed");
        assertNull(result.getCreationDate(), "creationdate failed");
    }

    @Test
    void userToDBO() {
        var user = createDefaultUser();

        var result = mappingService.userToDao(user);

        assertEquals(DEFAULT_USER_NAME, result.getName(), "name failed");
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail(), "email failed");
        assertEquals(DEFAULT_USER_ID, result.getId(), "id failed");
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey(), "publickey failed");
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate(), "creationdate failed");
    }

    @Test
    void userToResponse() {
        var user = createDefaultUser();

        var result = mappingService.userToResponse(user);

        assertEquals(DEFAULT_USER_NAME, result.getName());
        assertEquals(DEFAULT_USER_LOCALE, result.getLocale());
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertEquals(DEFAULT_USER_ID, result.getId());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate());
    }

    @Test
    void userDBOToSimpleResponse() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userDaoToSimpleResponse(userDBO);

        assertNotNull(result);
    }

    @Test
    void userDBOToResponse() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userDaoToResponse(userDBO);

        assertNotNull(result);
    }
}
