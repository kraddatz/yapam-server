package app.yapam;

import app.yapam.secret.model.Secret;
import app.yapam.secret.model.SecretTypeEnum;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.repository.SecretDao;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDao;
import org.junit.jupiter.api.Disabled;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;

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

    protected final String DEFAUlT_SECRET_TITLE = "secretTitle";
    protected final String DEFAUlT_SECRET_DATA = "secretData";
    protected final String DEFAUlT_SECRET_SECRETID = "4c7e1860-5ae4-4c40-8645-9c5a52d1b007";
    protected final String DEFAUlT_SECRET_ID = "4c7e1860-5ae4-4c40-8645-9c5a52d1b007";
    protected final LocalDateTime DEFAUlT_SECRET_CREATION_DATE = LocalDateTime.now();
    protected final Integer DEFAUlT_SECRET_VERSION = 1;
    protected final SecretTypeEnum DEFAUlT_SECRET_TYPE = SecretTypeEnum.LOGIN;

    protected final String DEFAULT_USER_ID = "fec7a584-fe73-4a0a-975c-c23f5b9632f8";
    protected final String DEFAULT_USER_LOCALE = "de-DE";
    protected final String DEFAULT_USER_EMAIL = "user@email.com";
    protected final String DEFAULT_USER_NAME = "Name";
    protected final String DEFAULT_USER_PUBLIC_KEY = "publicKey";
    protected final LocalDateTime DEFAULT_USER_CREATION_DATE = LocalDateTime.now();

    protected SecretResponse createDefaultSecretResponse() {
        var secretResponse = new SecretResponse();
        secretResponse.setCreationDate(DEFAUlT_SECRET_CREATION_DATE);
        secretResponse.setData(DEFAUlT_SECRET_DATA);
        secretResponse.setSecretId(DEFAUlT_SECRET_SECRETID);
        secretResponse.setTitle(DEFAUlT_SECRET_TITLE);
        secretResponse.setType(DEFAUlT_SECRET_TYPE);
        secretResponse.setUser(createDefaultSimpleUserResponse());
        secretResponse.setVersion(DEFAUlT_SECRET_VERSION);
        return secretResponse;
    }

    protected Secret createDefaultSecret() {
        var secret = new Secret();
        secret.setTitle(DEFAUlT_SECRET_TITLE);
        secret.setData(DEFAUlT_SECRET_DATA);
        secret.setSecretId(DEFAUlT_SECRET_SECRETID);
        secret.setCreationDate(DEFAUlT_SECRET_CREATION_DATE);
        secret.setUser(createDefaultUser());
        secret.setVersion(DEFAUlT_SECRET_VERSION);
        secret.setType(DEFAUlT_SECRET_TYPE);
        return secret;
    }

    protected SecretDao createDefaultSecretDBO() {
        var secretDBO = new SecretDao();
        secretDBO.setTitle(DEFAUlT_SECRET_TITLE);
        secretDBO.setData(DEFAUlT_SECRET_DATA);
        secretDBO.setType(DEFAUlT_SECRET_TYPE);
        secretDBO.setUser(createDefaultUserDao());
        secretDBO.setCreationDate(DEFAUlT_SECRET_CREATION_DATE);
        secretDBO.setId(DEFAUlT_SECRET_ID);
        secretDBO.setVersion(DEFAUlT_SECRET_VERSION);
        secretDBO.setSecretId(DEFAUlT_SECRET_SECRETID);
        return secretDBO;
    }

    protected SecretRequest createDefaultSecretRequest() {
        var secretRequest = new SecretRequest();
        secretRequest.setTitle(DEFAUlT_SECRET_TITLE);
        secretRequest.setData(DEFAUlT_SECRET_DATA);
        secretRequest.setType(DEFAUlT_SECRET_TYPE);
        secretRequest.setUserId(DEFAULT_USER_ID);
        return secretRequest;
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

    protected MockHttpServletRequest createDefaultHttpServletRequest() {
        return new MockHttpServletRequest();
    }

}
