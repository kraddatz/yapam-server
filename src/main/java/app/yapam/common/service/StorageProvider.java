package app.yapam.common.service;

import app.yapam.common.error.InternalErrorException;
import app.yapam.common.repository.FileRepository;
import app.yapam.config.YapamProperties;
import app.yapam.file.model.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class StorageProvider {

    private static Log log = LogFactory.getLog(StorageProvider.class);
    @Autowired private YapamProperties.StorageProviderConfiguration storageProviderConfiguration;
    @Autowired private FileRepository fileRepository;
    @Autowired private MappingService mappingService;

    private String getFilePath(String fileHash) {
        return storageProviderConfiguration.getRootPath() +
                java.io.File.separator +
                fileHash.substring(0, 3) +
                java.io.File.separator +
                fileHash.substring(3);
    }

    public abstract byte[] readContent(Path filepath) throws IOException;

    public File readFile(String fileId) {
        try {
            var fileDao = fileRepository.findOneById(fileId);
            var filePath = getFilePath(fileDao.getHash());
            var content = readContent(Paths.get(filePath));
            var file = mappingService.fileFromDao(fileDao);
            file.setContent(content);
            return file;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalErrorException("Unable to read the file content");
        }
    }

    public abstract void storeContent(byte[] content, Path filepath) throws IOException;

    public void storeFile(File file, String fileId) {
        try {
            var fileDao = fileRepository.findOneById(fileId);
            var filePath = Paths.get(getFilePath(fileDao.getHash()));
            if (!Files.exists(filePath)) {
                storeContent(file.getContent(), filePath);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InternalErrorException("Unable to write the file content");
        }
    }
}
