package app.yapam;

import app.yapam.common.repository.*;
import app.yapam.file.model.File;
import app.yapam.file.model.response.SimpleFileResponse;
import app.yapam.secret.model.Secret;
import app.yapam.secret.model.SecretTypeEnum;
import app.yapam.secret.model.UserSecretPrivilege;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.secret.model.request.UserIdSecretPrivilege;
import app.yapam.secret.model.response.SecretResponse;
import app.yapam.secret.model.response.SimpleSecretResponse;
import app.yapam.secret.model.response.SimpleUserPrivilegeResponse;
import app.yapam.tag.model.Tag;
import app.yapam.tag.model.request.TagRequestWrapper;
import app.yapam.tag.model.response.TagResponse;
import app.yapam.tag.model.response.TagResponseWrapper;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.user.model.response.UserResponse;
import org.junit.jupiter.api.Disabled;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
@SuppressWarnings("squid:S2187")
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
    protected final String API_FILES_BASE_URL = "/api/files";
    protected final String API_FILES_FILE_BY_ID = API_FILES_BASE_URL + "/{fileId}";
    protected final String API_TAGS_BASE_URL = "/api/tags";

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

    protected final String DEFAULT_FILE_ID = "a532ef68-771f-4b78-a124-61781f39ca10";
    protected final String DEFAULT_FILE_FILENAME = "filename.pdf";
    protected final Long DEFAULT_FILE_FILESIZE = 12L;
    protected final String DEFAULT_FILE_DATA = "somepdfstuff";
    protected final String DEFAULT_FILE_HASH = "nhe6T2Lb1yaG9Xcf4oAPnBfQsCs=";
    protected final String DEFAULT_FILE_MIMETYPE = "application/pdf";

    protected final String DEFAULT_TAG_ID = "804ba1f6-0732-4b98-9f15-66c9666177c9";
    protected final String DEFAULT_TAG_NAME = "testtag";

    protected void mockSecurityContextHolder() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(DEFAULT_USER_ID);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    protected File createDefaultFile() {
        var file = new File();
        file.setId(DEFAULT_FILE_ID);
        file.setFilesize(DEFAULT_FILE_FILESIZE);
        file.setFilename(DEFAULT_FILE_FILENAME);
        file.setHash(DEFAULT_FILE_HASH);
        file.setMimetype(DEFAULT_FILE_MIMETYPE);

        return file;
    }

    protected FileDao createDefaultFileDao() {
        var fileDao = new FileDao();
        List<SecretDao> secretDaos = new ArrayList<>();
        secretDaos.add(createDefaultSecretDao());
        fileDao.setId(DEFAULT_FILE_ID);
        fileDao.setSecrets(secretDaos);
        fileDao.setFilename(DEFAULT_FILE_FILENAME);
        fileDao.setFilesize(DEFAULT_FILE_FILESIZE);
        fileDao.setHash(DEFAULT_FILE_HASH);
        fileDao.setMimetype(DEFAULT_FILE_MIMETYPE);

        return fileDao;
    }

    protected MockHttpServletRequest createDefaultHttpServletRequest() {
        return new MockHttpServletRequest();
    }

    protected MockMultipartFile createDefaultMultipartFile() {
        return new MockMultipartFile("file", "filename.pdf", "application/pdf", "somepdfstuff".getBytes());
    }

    protected Resource createDefaultResource() {
        return new ByteArrayResource(DEFAULT_FILE_DATA.getBytes());
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
        secretDBO.setUsers(Collections.singletonList(userSecretDao));
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
        secretRequest.setFiles(Collections.singletonList(DEFAULT_FILE_ID));
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

    protected SimpleFileResponse createDefaultSimpleFileResponse() {
        var simpleFileResponse = new SimpleFileResponse();
        simpleFileResponse.setFilesize(DEFAULT_FILE_FILESIZE);
        simpleFileResponse.setFilename(DEFAULT_FILE_FILENAME);
        simpleFileResponse.setId(DEFAULT_FILE_ID);

        return simpleFileResponse;
    }

    protected SimpleSecretResponse createDefaultSimpleSecretResponse() {
        var simpleSecretResponse = new SimpleSecretResponse();
        simpleSecretResponse.setSecretId(DEFAULT_SECRET_SECRETID);
        simpleSecretResponse.setTitle(DEFAULT_SECRET_TITLE);
        simpleSecretResponse.setTags(Collections.singletonList(DEFAULT_TAG_NAME));
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

    protected Tag createDefaultTag() {
        var tag = new Tag();
        tag.setId(DEFAULT_TAG_ID);
        tag.setName(DEFAULT_TAG_NAME);
        return tag;
    }

    protected TagDao createDefaultTagDao() {
        var tagDao = new TagDao();
        tagDao.setId(DEFAULT_TAG_ID);
        tagDao.setName(DEFAULT_TAG_NAME);
        tagDao.setSecrets(new ArrayList<>());
        return tagDao;
    }

    protected TagRequestWrapper createDefaultTagRequestWrapper() {
        var tagRequestWrapper = new TagRequestWrapper();
        tagRequestWrapper.setTags(Collections.singletonList(DEFAULT_TAG_NAME));

        return tagRequestWrapper;
    }

    protected TagResponse createDefaultTagResponse() {
        var tagResponse = new TagResponse();
        tagResponse.setId(DEFAULT_TAG_ID);
        tagResponse.setName(DEFAULT_TAG_NAME);

        return tagResponse;
    }

    protected TagResponseWrapper createDefaultTagResponseWrapper() {
        var tagResponseWrapper = new TagResponseWrapper();
        tagResponseWrapper.setTags(Collections.singletonList(createDefaultTagResponse()));

        return tagResponseWrapper;
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
        userRequest.setEmail(DEFAULT_USER_EMAIL);
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
