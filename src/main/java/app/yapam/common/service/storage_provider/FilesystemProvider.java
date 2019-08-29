package app.yapam.common.service.storage_provider;

import app.yapam.common.error.InternalErrorException;
import app.yapam.common.repository.FileRepository;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.StorageProvider;
import app.yapam.config.YapamProperties;
import app.yapam.file.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ConditionalOnProperty(name = "yapam.storage-provider", havingValue = "FILESYSTEM")
@Service
@Slf4j
public class FilesystemProvider implements StorageProvider {

    @Autowired private YapamProperties.StorageProviderConfiguration storageProviderConfiguration;
    @Autowired private FileRepository fileRepository;
    @Autowired private MappingService mappingService;

    @Override
    public File readFile(String fileId) {
        try {
            var fileDao = fileRepository.findOneById(fileId);
            var filePath = storageProviderConfiguration.getRootPath() +
                    java.io.File.separator +
                    fileDao.getHash().substring(0, 3) +
                    java.io.File.separator +
                    fileDao.getHash().substring(3);
            var content = Files.readAllBytes(Paths.get(filePath));
            var file = mappingService.fileFromDao(fileDao);
            file.setContent(content);
            return file;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalErrorException("Unable to read the file content");
        }
    }

    @Override
    public void storeFile(byte[] content, String fileHash) {
        var filePath = new java.io.File(storageProviderConfiguration.getRootPath() +
                java.io.File.separator +
                fileHash.substring(0, 3) +
                java.io.File.separator +
                fileHash.substring(3));
        new java.io.File(filePath.getParent()).mkdir();

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(content);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
