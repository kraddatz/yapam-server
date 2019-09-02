package app.yapam.file;

import app.yapam.YapamBaseTest;
import app.yapam.common.repository.FileDao;
import app.yapam.common.repository.FileRepository;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.StorageProvider;
import app.yapam.file.model.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileService.class)
@ActiveProfiles("test")
public class FileServiceTest extends YapamBaseTest {

    @Autowired private FileService fileService;
    @MockBean private StorageProvider storageProvider;
    @MockBean private FileRepository fileRepository;
    @MockBean private MappingService mappingService;

    @Test
    void getFileForId() {
        var file = createDefaultFile();
        var fileResponse = createDefaultFileResponse();
        when(storageProvider.readFile(DEFAULT_FILE_ID)).thenReturn(file);
        when(mappingService.fileToResponse(file)).thenReturn(fileResponse);

        var result = fileService.getFileForId(DEFAULT_FILE_ID);

        assertNotNull(result);
    }

    @Test
    void saveFile() {
        var fileRequest = createDefaultMultipartFile();
        var file = createDefaultFile();
        var fileDao = createDefaultFileDao();
        var simpleFileResponse = createDefaultSimpleFileResponse();
        when(mappingService.fileFromRequest(fileRequest)).thenReturn(file);
        when(mappingService.fileToDao(file)).thenReturn(fileDao);
        when(fileRepository.save(fileDao)).thenReturn(fileDao);
        when(mappingService.fileDaoToSimpleResponse(fileDao)).thenReturn(simpleFileResponse);

        var result = fileService.saveFile(fileRequest);

        assertNotNull(result);
    }

    @Test
    void attachSecretToFiles() {
        var fileDao = createDefaultFileDao();
        var secretDao = createDefaultSecretDao();
        var file = createDefaultFile();
        when(mappingService.fileToDao(file)).thenReturn(fileDao);

        fileService.attachSecretToFiles(Collections.singletonList(file), secretDao);

        verify(fileRepository, times(1)).save(any(FileDao.class));
    }

    @Test
    void whenSaveSameFileMultipleTimes_thenStoreFileOnce() {
        var fileRequest = createDefaultMultipartFile();
        var file = createDefaultFile();
        var fileDao = createDefaultFileDao();
        var simpleFileResponse = createDefaultSimpleFileResponse();
        when(mappingService.fileFromRequest(fileRequest)).thenReturn(file);
        when(mappingService.fileToDao(file)).thenReturn(fileDao);
        when(fileRepository.save(fileDao)).thenReturn(fileDao);
        when(mappingService.fileDaoToSimpleResponse(fileDao)).thenReturn(simpleFileResponse);
        when(fileRepository.findOneByHash(DEFAULT_FILE_HASH)).thenReturn(null, fileDao);

        var result = fileService.saveFile(fileRequest);
        result = fileService.saveFile(fileRequest);

        verify(fileRepository, times(1)).save(any(FileDao.class));
        verify(storageProvider, times(1)).storeFile(any(File.class), anyString());

        assertNotNull(result);
    }
}
