package app.yapam.file;

import app.yapam.YapamBaseTest;
import app.yapam.common.error.UnknownFileException;
import app.yapam.common.repository.FileDao;
import app.yapam.common.repository.FileRepository;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.StorageProvider;
import app.yapam.file.model.File;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void attachSecretToFiles() {
        var fileDao = createDefaultFileDao();
        var secretDao = createDefaultSecretDao();
        var file = createDefaultFile();
        when(fileRepository.findOneById(DEFAULT_FILE_ID)).thenReturn(fileDao);

        fileService.attachSecretToFiles(Collections.singletonList(file), secretDao);

        ArgumentCaptor<List<FileDao>> captor = ArgumentCaptor.forClass(List.class);
        verify(fileRepository, times(1)).saveAll(captor.capture());

        var fileDaos = captor.getValue();
        assertEquals(DEFAULT_FILE_FILENAME, fileDaos.get(0).getFilename());
        assertEquals(DEFAULT_FILE_ID, fileDaos.get(0).getId());
        assertEquals(DEFAULT_FILE_HASH, fileDaos.get(0).getHash());
        assertEquals(DEFAULT_FILE_MIMETYPE, fileDaos.get(0).getMimetype());
        assertEquals(DEFAULT_FILE_FILESIZE, fileDaos.get(0).getFilesize());
    }

    @Test
    void attachSecretToFiles_whenFileNotFound_thenThrowUnknownFileException() {
        var secretDao = createDefaultSecretDao();
        var file = createDefaultFile();
        assertThrows(UnknownFileException.class, () -> fileService.attachSecretToFiles(Collections.singletonList(file), secretDao));
    }

    @Test
    void getFileForId() {
        var resource = createDefaultResource();
        when(storageProvider.readFile(DEFAULT_FILE_ID)).thenReturn(resource);

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
