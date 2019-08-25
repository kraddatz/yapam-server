package app.yapam;

import app.yapam.common.repository.SecretDao;
import app.yapam.common.repository.UserDao;
import app.yapam.common.repository.UserSecretDao;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.SecretTypeEnum;
import app.yapam.secret.model.UserSecretPrivilege;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.request.UserIdSecretPrivilege;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.secret.model.response.SimpleUserPrivilegeResponse;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import org.junit.jupiter.api.Disabled;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@Disabled
@java.lang.SuppressWarnings("squid:S2187")
public abstract class YapamBaseTest {

    protected final String DEFAULT_HOST_BASE_URL = "http://localhost";

    protected final String EMAIL_WELCOME_TEXT = "welcome";
    protected final String EMAIL_MESSAGE_SENDER = "yapam@yourdomain.com";

    protected final String API_STATUS_URL = "/api/status";
    protected final String API_HEALTH_URL = "/api/health";
    protected final String API_SECRETS_BASE_URL = "/api/secrets";
    protected final String API_SINGLE_SECRET_URL = API_SECRETS_BASE_URL + "/{secretId}";
    protected final String API_USERS_BASE_URL = "/api/users";
    protected final String API_USERS_USER_BY_ID = API_USERS_BASE_URL + "/{userId}";
    protected final String API_USERS_CURRENT_USER = API_USERS_BASE_URL + "/currentuser";

    protected final String DEFAULT_SECRET_TITLE = "secretTitle";
    protected final String DEFAULT_SECRET_DATA = "secretData";
    protected final String DEFAULT_SECRET_SECRETID = "4c7e1860-5ae4-4c40-8645-9c5a52d1b007";
    protected final String DEFAULT_SECRET_ID = "4c7e1860-5ae4-4c40-8645-9c5a52d1b007";
    protected final LocalDateTime DEFAULT_SECRET_CREATION_DATE = LocalDateTime.now();
    protected final Integer DEFAULT_SECRET_VERSION = 1;
    protected final SecretTypeEnum DEFAULT_SECRET_TYPE = SecretTypeEnum.LOGIN;

    protected final String DEFAULT_USER_ID = "fec7a584-fe73-4a0a-975c-c23f5b9632f8";
    protected final String DEFAULT_USER_LOCALE = "de-DE";
    protected final String DEFAULT_USER_EMAIL = "user@email.com";
    protected final String DEFAULT_USER_NAME = "Name";
    protected final String DEFAULT_USER_PUBLIC_KEY = "publicKey";
    protected final LocalDateTime DEFAULT_USER_CREATION_DATE = LocalDateTime.now();

    protected MockHttpServletRequest createDefaultHttpServletRequest() {
        return new MockHttpServletRequest();
    }

    protected Secret createDefaultSecret() {
        var secret = new Secret();
        var userSecretPrivilege = new UserSecretPrivilege(createDefaultUser(), true);
        secret.setTitle(DEFAULT_SECRET_TITLE);
        secret.setData(DEFAULT_SECRET_DATA);
        secret.setSecretId(DEFAULT_SECRET_SECRETID);
        secret.setCreationDate(DEFAULT_SECRET_CREATION_DATE);
        secret.setUsers(Collections.singletonList(userSecretPrivilege));
        secret.setVersion(DEFAULT_SECRET_VERSION);
        secret.setType(DEFAULT_SECRET_TYPE);
        return secret;
    }

    protected SecretDao createDefaultSecretDao() {
        var secretDBO = new SecretDao();
        var userSecretDao = new UserSecretDao(secretDBO, createDefaultUserDao(), true);
        secretDBO.setTitle(DEFAULT_SECRET_TITLE);
        secretDBO.setData(DEFAULT_SECRET_DATA);
        secretDBO.setType(DEFAULT_SECRET_TYPE);
        secretDBO.setUsers(new HashSet<>(Collections.singletonList(userSecretDao)));
        secretDBO.setCreationDate(DEFAULT_SECRET_CREATION_DATE);
        secretDBO.setId(DEFAULT_SECRET_ID);
        secretDBO.setVersion(DEFAULT_SECRET_VERSION);
        secretDBO.setSecretId(DEFAULT_SECRET_SECRETID);
        return secretDBO;
    }

    protected SecretRequest createDefaultSecretRequest() {
        var secretRequest = new SecretRequest();
        var userIdSecretPrivilege = new UserIdSecretPrivilege(DEFAULT_USER_ID, true);
        secretRequest.setTitle(DEFAULT_SECRET_TITLE);
        secretRequest.setData(DEFAULT_SECRET_DATA);
        secretRequest.setType(DEFAULT_SECRET_TYPE);
        secretRequest.setUsers(Collections.singletonList(userIdSecretPrivilege));
        return secretRequest;
    }

    protected SecretResponse createDefaultSecretResponse() {
        var secretResponse = new SecretResponse();
        var simpleUserPrivilegeResponse = new SimpleUserPrivilegeResponse(createDefaultSimpleUserResponse(), true);
        secretResponse.setCreationDate(DEFAULT_SECRET_CREATION_DATE);
        secretResponse.setData(DEFAULT_SECRET_DATA);
        secretResponse.setSecretId(DEFAULT_SECRET_SECRETID);
        secretResponse.setTitle(DEFAULT_SECRET_TITLE);
        secretResponse.setType(DEFAULT_SECRET_TYPE);
        secretResponse.setUsers(Collections.singletonList(simpleUserPrivilegeResponse));
        secretResponse.setVersion(DEFAULT_SECRET_VERSION);
        return secretResponse;
    }

    protected SimpleSecretResponse createDefaultSimpleSecretResponse() {
        var simpleSecretResponse = new SimpleSecretResponse();
        simpleSecretResponse.setSecretId(DEFAULT_SECRET_SECRETID);
        simpleSecretResponse.setTitle(DEFAULT_SECRET_TITLE);
        return simpleSecretResponse;
    }

    protected SimpleUserResponse createDefaultSimpleUserResponse() {
        var simpleUserResponse = new SimpleUserResponse();
        simpleUserResponse.setEmail(DEFAULT_USER_EMAIL);
        simpleUserResponse.setId(DEFAULT_USER_ID);
        simpleUserResponse.setName(DEFAULT_USER_NAME);
        simpleUserResponse.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        return simpleUserResponse;
    }

    protected User createDefaultUser() {
        var user = new User();
        user.setId(DEFAULT_USER_ID);
        user.setCreationDate(DEFAULT_USER_CREATION_DATE);
        user.setLocale(DEFAULT_USER_LOCALE);
        user.setEmail(DEFAULT_USER_EMAIL);
        user.setName(DEFAULT_USER_NAME);
        user.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        return user;
    }

    protected UserDao createDefaultUserDao() {
        var userDBO = new UserDao();
        userDBO.setId(DEFAULT_USER_ID);
        userDBO.setCreationDate(DEFAULT_USER_CREATION_DATE);
        userDBO.setEmail(DEFAULT_USER_EMAIL);
        userDBO.setName(DEFAULT_USER_NAME);
        userDBO.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        userDBO.setLocale(DEFAULT_USER_LOCALE);
        return userDBO;
    }

    protected UserRequest createDefaultUserRequest() {
        var userRequest = new UserRequest();
        userRequest.setLocale(DEFAULT_USER_LOCALE);
        userRequest.setName(DEFAULT_USER_NAME);
        userRequest.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        return userRequest;
    }

    protected UserResponse createDefaultUserResponse() {
        var userResponse = new UserResponse();
        userResponse.setName(DEFAULT_USER_NAME);
        userResponse.setEmail(DEFAULT_USER_EMAIL);
        userResponse.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        userResponse.setLocale(DEFAULT_USER_LOCALE);
        userResponse.setCreationDate(DEFAULT_USER_CREATION_DATE);
        userResponse.setId(DEFAULT_USER_ID);
        return userResponse;
    }

    protected UserSecretDao createDefaultUserSecretDao() {
        var userSecretDao = new UserSecretDao();
        userSecretDao.setPrivileged(true);
        userSecretDao.setUser(createDefaultUserDao());
        userSecretDao.setSecret(createDefaultSecretDao());
        return userSecretDao;
    }
}
