package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import app.yapam.common.repository.FileRepository;
import app.yapam.common.repository.TagRepository;
import app.yapam.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MappingService.class)
@ActiveProfiles("test")
class MappingServiceTest extends YapamBaseTest {

    @Autowired private MappingService mappingService;
    @MockBean private UserRepository userRepository;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private FileRepository fileRepository;
    @MockBean private TagRepository tagRepository;

    @Test
    void fileDaoToSimpleResponse() {
        var fileDao = createDefaultFileDao();

        var result = mappingService.fileDaoToSimpleResponse(fileDao);

        assertEquals(DEFAULT_FILE_FILESIZE, result.getFilesize());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFilename());
        assertEquals(DEFAULT_FILE_ID, result.getId());
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getMimetype());
    }

    @Test
    void fileFromDao() {
        var fileDao = createDefaultFileDao();

        var result = mappingService.fileFromDao(fileDao);

        assertEquals(DEFAULT_FILE_MIMETYPE, result.getMimetype());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFilename());
        assertEquals(DEFAULT_FILE_HASH, result.getHash());
        assertEquals(DEFAULT_FILE_ID, result.getId());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFilesize());
    }

    @Test
    void fileFromRequest() {
        var fileRequest = createDefaultMultipartFile();

        var result = mappingService.fileFromRequest(fileRequest);

        assertEquals(DEFAULT_FILE_MIMETYPE, result.getMimetype());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFilesize());
        assertNull(result.getId());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFilename());
        assertEquals(DEFAULT_FILE_HASH, result.getHash());
    }

    @Test
    void fileToDao() {
        var file = createDefaultFile();

        var result = mappingService.fileToDao(file);

        assertEquals(DEFAULT_FILE_FILENAME, result.getFilename());
        assertEquals(DEFAULT_FILE_ID, result.getId());
        assertEquals(DEFAULT_FILE_HASH, result.getHash());
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getMimetype());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFilesize());
    }

    @Test
    void fileToSimpleResponse() {
        var file = createDefaultFile();

        var result = mappingService.fileToSimpleResponse(file);

        assertEquals(DEFAULT_FILE_MIMETYPE, result.getMimetype());
        assertEquals(DEFAULT_FILE_ID, result.getId());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFilename());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFilesize());
    }

    @Test
    void secretDaoToResponse() {
        var secretDao = createDefaultSecretDao();
        secretDao.setFiles(Collections.singletonList(createDefaultFileDao()));
        secretDao.setTags(Collections.singletonList(createDefaultTagDao()));

        var result = mappingService.secretDaoToResponse(secretDao);

        assertTrue(result.getUsers().get(0).getPrivileged());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertEquals(DEFAULT_USER_EMAIL, result.getUsers().get(0).getUser().getEmail());
        assertEquals(DEFAULT_USER_ID, result.getUsers().get(0).getUser().getId());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getUsers().get(0).getUser().getPublicKey());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFiles().get(0).getFilesize());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFiles().get(0).getFilename());
        assertEquals(DEFAULT_FILE_ID, result.getFiles().get(0).getId());
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getFiles().get(0).getMimetype());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0));
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
    }

    @Test
    void secretDaoToSimpleResponse() {
        var secretDao = createDefaultSecretDao();
        secretDao.setFiles(Collections.singletonList(createDefaultFileDao()));
        secretDao.setTags(Collections.singletonList(createDefaultTagDao()));

        var result = mappingService.secretDaoToSimpleResponse(secretDao);

        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
    }

    @Test
    void secretFromDao() {
        var secretDao = createDefaultSecretDao();
        secretDao.setFiles(Collections.singletonList(createDefaultFileDao()));
        secretDao.setTags(Collections.singletonList(createDefaultTagDao()));

        var result = mappingService.secretFromDao(secretDao);

        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertEquals(DEFAULT_USER_EMAIL, result.getUsers().get(0).getUser().getEmail());
        assertEquals(DEFAULT_USER_ID, result.getUsers().get(0).getUser().getId());
        assertEquals(DEFAULT_USER_LOCALE, result.getUsers().get(0).getUser().getLocale());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getUsers().get(0).getUser().getPublicKey());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getUsers().get(0).getUser().getCreationDate());
        assertEquals(DEFAULT_FILE_HASH, result.getFiles().get(0).getHash());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFiles().get(0).getFilename());
        assertEquals(DEFAULT_FILE_ID, result.getFiles().get(0).getId());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFiles().get(0).getFilesize());
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getFiles().get(0).getMimetype());
        assertEquals(DEFAULT_TAG_ID, result.getTags().get(0).getId());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0).getName());
        assertTrue(result.getUsers().get(0).getPrivilege());
    }

    @Test
    void secretFromRequest() {
        var secretRequest = createDefaultSecretRequest();
        secretRequest.setTags(Collections.singletonList(DEFAULT_TAG_ID));
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(createDefaultUserDao());
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(createDefaultFileDao());
        when(tagRepository.findOneById(DEFAULT_TAG_ID)).thenReturn(createDefaultTagDao());

        var result = mappingService.secretFromRequest(secretRequest);

        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getUsers().get(0).getUser().getCreationDate());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getUsers().get(0).getUser().getPublicKey());
        assertEquals(DEFAULT_USER_LOCALE, result.getUsers().get(0).getUser().getLocale());
        assertEquals(DEFAULT_USER_ID, result.getUsers().get(0).getUser().getId());
        assertEquals(DEFAULT_USER_EMAIL, result.getUsers().get(0).getUser().getEmail());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0).getName());
        assertEquals(DEFAULT_TAG_ID, result.getTags().get(0).getId());
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getFiles().get(0).getMimetype());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFiles().get(0).getFilesize());
        assertEquals(DEFAULT_FILE_ID, result.getFiles().get(0).getId());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFiles().get(0).getFilename());
        assertEquals(DEFAULT_FILE_HASH, result.getFiles().get(0).getHash());
        assertTrue(result.getUsers().get(0).getPrivilege());
    }

    @Test
    void secretToDao() {
        var secret = createDefaultSecret();
        secret.setFiles(Collections.singletonList(createDefaultFile()));
        secret.setTags(Collections.singletonList(createDefaultTag()));

        var result = mappingService.secretToDao(secret);

        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getUsers().get(0).getUser().getCreationDate());
        assertEquals(DEFAULT_USER_LOCALE, result.getUsers().get(0).getUser().getLocale());
        assertEquals(DEFAULT_USER_ID, result.getUsers().get(0).getUser().getId());
        assertEquals(DEFAULT_USER_EMAIL, result.getUsers().get(0).getUser().getEmail());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0).getName());
        assertEquals(DEFAULT_TAG_ID, result.getTags().get(0).getId());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFiles().get(0).getFilesize());
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getFiles().get(0).getMimetype());
        assertEquals(DEFAULT_FILE_HASH, result.getFiles().get(0).getHash());
        assertEquals(DEFAULT_FILE_ID, result.getFiles().get(0).getId());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFiles().get(0).getFilename());
        assertTrue(result.getUsers().get(0).getPrivileged());
    }

    @Test
    void secretToResponse() {
        var secret = createDefaultSecret();
        secret.setFiles(Collections.singletonList(createDefaultFile()));
        secret.setTags(Collections.singletonList(createDefaultTag()));

        var result = mappingService.secretToResponse(secret);

        assertEquals(DEFAULT_SECRET_TYPE, result.getType());
        assertEquals(DEFAULT_SECRET_TITLE, result.getTitle());
        assertEquals(DEFAULT_SECRET_SECRETID, result.getSecretId());
        assertEquals(DEFAULT_SECRET_CREATION_DATE, result.getCreationDate());
        assertEquals(DEFAULT_SECRET_VERSION, result.getVersion());
        assertEquals(DEFAULT_SECRET_DATA, result.getData());
        assertEquals(DEFAULT_USER_NAME, result.getUsers().get(0).getUser().getName());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getUsers().get(0).getUser().getPublicKey());
        assertEquals(DEFAULT_USER_EMAIL, result.getUsers().get(0).getUser().getEmail());
        assertEquals(DEFAULT_USER_ID, result.getUsers().get(0).getUser().getId());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0));
        assertEquals(DEFAULT_FILE_MIMETYPE, result.getFiles().get(0).getMimetype());
        assertEquals(DEFAULT_FILE_ID, result.getFiles().get(0).getId());
        assertEquals(DEFAULT_FILE_FILENAME, result.getFiles().get(0).getFilename());
        assertEquals(DEFAULT_FILE_FILESIZE, result.getFiles().get(0).getFilesize());
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
    void tagDaoToResponse() {
        var tagDao = createDefaultTagDao();

        var result = mappingService.tagDaoToResponse(tagDao);

        assertEquals(DEFAULT_TAG_ID, result.getId());
        assertEquals(DEFAULT_TAG_NAME, result.getName());
    }

    @Test
    void tagFromDao() {
        var tagDao = createDefaultTagDao();

        var result = mappingService.tagFromDao(tagDao);

        assertEquals(DEFAULT_TAG_ID, result.getId());
        assertEquals(DEFAULT_TAG_NAME, result.getName());
    }

    @Test
    void tagToDao() {
        var tag = createDefaultTag();

        var result = mappingService.tagToDao(tag);

        assertEquals(DEFAULT_TAG_ID, result.getId());
        assertEquals(DEFAULT_TAG_NAME, result.getName());
    }

    @Test
    void userDBOToResponse() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userDaoToResponse(userDBO);

        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertEquals(DEFAULT_USER_ID, result.getId());
        assertEquals(DEFAULT_USER_LOCALE, result.getLocale());
        assertEquals(DEFAULT_USER_NAME, result.getName());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate());
    }

    @Test
    void userDBOToSimpleResponse() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userDaoToSimpleResponse(userDBO);

        assertEquals(DEFAULT_USER_ID, result.getId());
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertEquals(DEFAULT_USER_NAME, result.getName());

        assertNotNull(result);
    }

    @Test
    void userFromDBO() {
        var userDBO = createDefaultUserDao();

        var result = mappingService.userFromDao(userDBO);

        assertEquals(DEFAULT_USER_NAME, result.getName());
        assertEquals(DEFAULT_USER_LOCALE, result.getLocale());
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertEquals(DEFAULT_USER_ID, result.getId());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate());
    }

    @Test
    void userFromRequest() {
        var userRequest = createDefaultUserRequest();
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);

        var result = mappingService.userFromRequest(userRequest);

        assertEquals(DEFAULT_USER_NAME, result.getName());
        assertEquals(DEFAULT_USER_LOCALE, result.getLocale());
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertNull(result.getId());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertNull(result.getCreationDate());
    }

    @Test
    void userToDBO() {
        var user = createDefaultUser();

        var result = mappingService.userToDao(user);

        assertEquals(DEFAULT_USER_NAME, result.getName());
        assertEquals(DEFAULT_USER_EMAIL, result.getEmail());
        assertEquals(DEFAULT_USER_ID, result.getId());
        assertEquals(DEFAULT_USER_PUBLIC_KEY, result.getPublicKey());
        assertEquals(DEFAULT_USER_CREATION_DATE, result.getCreationDate());
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
