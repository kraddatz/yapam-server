package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.common.repository.UserRepository;
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
    void secretDaoToResponse() {
        var secretDao = createDefaultSecretDao();

        var result = mappingService.secretDaoToResponse(secretDao);

        assertNotNull(result);
    }

    @Test
    void secretDaoToSimpleResponse() {
        var secretDao = createDefaultSecretDao();

        var result = mappingService.secretDaoToSimpleResponse(secretDao);

        assertNotNull(result);
    }

    @Test
    void secretFromDao() {
        var secretDao = createDefaultSecretDao();

        var result = mappingService.secretFromDao(secretDao);

        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertTrue(result.getUsers().get(0).getPrivilege());
    }

    @Test
    void secretFromRequest() {
        var secretRequest = createDefaultSecretRequest();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(createDefaultUserDao());

        var result = mappingService.secretFromRequest(secretRequest);

        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertTrue(result.getUsers().get(0).getPrivilege());
    }

    @Test
    void secretToDao() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToDao(secret);

        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().iterator().next().getUser().getName());
        assertTrue(result.getUsers().iterator().next().getPrivileged());
    }

    @Test
    void secretToResponse() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToResponse(secret);

        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertTrue(result.getUsers().get(0).getPrivileged());
    }

    @Test
    void secretToSimpleResponse() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToSimpleResponse(secret);

        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
    }

    @Test
    void userDBOToResponse() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userDaoToResponse(userDBO);

        assertNotNull(result);
    }

    @Test
    void userDBOToSimpleResponse() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userDaoToSimpleResponse(userDBO);

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
}
