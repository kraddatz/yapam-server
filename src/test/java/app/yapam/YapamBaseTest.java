package app.yapam;

import app.yapam.config.YapamProperties;
import app.yapam.secret.SecretController;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.SecretTypeEnum;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.repository.SecretDBO;
import app.yapam.user.UserController;
import app.yapam.user.model.User;
import app.yapam.user.model.request.PasswordChangeRequest;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDBO;
import org.junit.jupiter.api.Disabled;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;

@Disabled
@java.lang.SuppressWarnings("squid:S2187")
public class YapamBaseTest {

    protected final String DEFAULT_HOST_BASE_URL = "http://localhost";

    protected final String EMAIL_GENERIC_EMAIL_VERIFY_URL = DEFAULT_HOST_BASE_URL + "/users/%s/email/verify?token=%s";
    protected final String EMAIL_GENERIC_EMAIL_CHANGE_URL = DEFAULT_HOST_BASE_URL + "/users/%s/email/change?email=%s&token=%s";
    protected final String EMAIL_MESSAGE_SENDER = "yapam@yourdomain.com";

    protected final String API_STATUS_URL = "/api/status";
    protected final String API_HEALTH_URL = "/api/health";
    protected final String API_KDF_INFO_URL = "/api/kdf";
    protected final String API_SECRETS_BASE_URL = "/api/secrets";
    protected final String API_SINGLE_SECRET_URL = API_SECRETS_BASE_URL + "/{secretId}";
    protected final String API_USERS_BASE_URL = "/api/users";
    protected final String API_USERS_EMAIL_VERIFY_URL = API_USERS_BASE_URL + "/{userId}/email/verify";
    protected final String API_USERS_EMAIL_CHANGE = API_USERS_BASE_URL + "/{userId}/email/change";
    protected final String API_USERS_CURRENT_USER = API_USERS_BASE_URL + "/currentuser";
    protected final String API_USERS_CURRENT_USER_EMAIL_REQUEST_CHANGE = API_USERS_CURRENT_USER + "/email/request-change";
    protected final String API_USERS_CURRENT_USER_PASSWORD_CHANGE = API_USERS_CURRENT_USER + "/password/change";

    protected final String API_HEADER_TOKEN = "token";
    protected final String API_HEADER_EMAIL = "email";

    protected final String DEFAUlT_SECRET_TITLE = "secretTitle";
    protected final String DEFAUlT_SECRET_DATA = "secretData";
    protected final String DEFAUlT_SECRET_SECRETID = "4c7e1860-5ae4-4c40-8645-9c5a52d1b007";
    protected final String DEFAUlT_SECRET_ID = "4c7e1860-5ae4-4c40-8645-9c5a52d1b007";
    protected final LocalDateTime DEFAUlT_SECRET_CREATION_DATE = LocalDateTime.now();
    protected final Integer DEFAUlT_SECRET_VERSION = 1;
    protected final SecretTypeEnum DEFAUlT_SECRET_TYPE = SecretTypeEnum.PASSWORD;

    protected final String DEFAULT_USER_ID = "fec7a584-fe73-4a0a-975c-c23f5b9632f8";
    protected final String DEFAULT_USER_CULTURE = "de-DE";
    protected final String DEFAULT_USER_EMAIL = "user@email.com";
    protected final String DEFAULT_USER_PASSWORD_HASH = "$2a$10$HFeBQjv4d.iGubQvGZe31uMBxWqoaHLQt9O1na7KlFZKhxvPkf7ge";
    protected final String DEFAULT_USER_PASSWORD_HINT = "passwordispassword";
    protected final String DEFAULT_USER_NAME = "Name";
    protected final String DEFAULT_USER_PUBLIC_KEY = "publicKey";
    protected final String DEFAULT_USER_EMAIL_TOKEN = "66dc10dc-114d-498e-9ff4-65613084eced";
    protected final LocalDateTime DEFAULT_USER_CREATION_DATE = LocalDateTime.now();
    protected final Boolean DEFAULT_USER_EMAIL_VERIFIED = true;
    protected final String NEW_USER_PASSWORD = "$2a$10$UGqevBtOypBHAbCAcBN2POIo4zWM3YbUSGHtAcm52osTw6RszcZF2";
    protected final String NEW_USER_PASSWORD_HINT = "passwordisnewpassword";
    protected final String NEW_USER_EMAIL = "newemail@email.com";
    protected final String NEW_USER_NAME = "newName";

    protected PasswordChangeRequest createDefaultPasswordChangeRequest() {
        var passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setMasterPasswordHash(NEW_USER_PASSWORD);
        passwordChangeRequest.setMasterPasswordHint(NEW_USER_PASSWORD_HINT);
        return passwordChangeRequest;
    }

    protected YapamProperties.YapamSecurity createDefaultYapamSecurityProperties() {
        var security = new YapamProperties.YapamSecurity();
        security.setBcryptIterations(10);
        return security;
    }

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

    protected SecretDBO createDefaultSecretDBO() {
        var secretDBO = new SecretDBO();
        secretDBO.setTitle(DEFAUlT_SECRET_TITLE);
        secretDBO.setData(DEFAUlT_SECRET_DATA);
        secretDBO.setType(DEFAUlT_SECRET_TYPE);
        secretDBO.setUser(createDefaultUserDBO());
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
        userRequest.setCulture(DEFAULT_USER_CULTURE);
        userRequest.setEmail(DEFAULT_USER_EMAIL);
        userRequest.setMasterPasswordHash(DEFAULT_USER_PASSWORD_HASH);
        userRequest.setMasterPasswordHint(DEFAULT_USER_PASSWORD_HINT);
        userRequest.setName(DEFAULT_USER_NAME);
        userRequest.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        return userRequest;
    }

    protected UserResponse createDefaultUserResponse() {
        var userResponse = new UserResponse();
        userResponse.setName(DEFAULT_USER_NAME);
        userResponse.setEmail(DEFAULT_USER_EMAIL);
        userResponse.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        userResponse.setEmailVerified(DEFAULT_USER_EMAIL_VERIFIED);
        userResponse.setCulture(DEFAULT_USER_CULTURE);
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
        user.setCulture(DEFAULT_USER_CULTURE);
        user.setEmail(DEFAULT_USER_EMAIL);
        user.setEmailToken(DEFAULT_USER_EMAIL_TOKEN);
        user.setMasterPasswordHash(DEFAULT_USER_PASSWORD_HASH);
        user.setMasterPasswordHint(DEFAULT_USER_PASSWORD_HINT);
        user.setName(DEFAULT_USER_NAME);
        user.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        user.setEmailVerified(DEFAULT_USER_EMAIL_VERIFIED);
        return user;
    }

    protected UserDBO createDefaultUserDBO() {
        var userDBO = new UserDBO();
        userDBO.setId(DEFAULT_USER_ID);
        userDBO.setCreationDate(DEFAULT_USER_CREATION_DATE);
        userDBO.setCulture(DEFAULT_USER_CULTURE);
        userDBO.setEmail(DEFAULT_USER_EMAIL);
        userDBO.setEmailToken(DEFAULT_USER_EMAIL_TOKEN);
        userDBO.setMasterPasswordHash(DEFAULT_USER_PASSWORD_HASH);
        userDBO.setMasterPasswordHint(DEFAULT_USER_PASSWORD_HINT);
        userDBO.setName(DEFAULT_USER_NAME);
        userDBO.setPublicKey(DEFAULT_USER_PUBLIC_KEY);
        userDBO.setEmailVerified(DEFAULT_USER_EMAIL_VERIFIED);
        return userDBO;
    }

    protected HandlerMethod createNoAnnotationHandlerMethod() throws NoSuchMethodException {
        var method = UserController.class.getMethod("createUser", UserRequest.class);

        return new HandlerMethod(new UserController(), method);
    }

    protected HandlerMethod createVerifiedEmailHandlerMethod() throws NoSuchMethodException {
        var method = SecretController.class.getMethod("createSecret", SecretRequest.class);

        return new HandlerMethod(new SecretController(), method);
    }

    protected MockHttpServletRequest createDefaultHttpServletRequest() {
        return new MockHttpServletRequest();
    }

    protected MockHttpServletResponse createDefaultHttpServletResponse() {
        return new MockHttpServletResponse();
    }

}
