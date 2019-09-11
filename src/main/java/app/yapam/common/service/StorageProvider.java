package app.yapam.common.service;

import app.yapam.common.error.InternalErrorException;
import app.yapam.common.error.UnknownFileException;
import app.yapam.common.repository.FileRepository;
import app.yapam.config.YapamProperties;
import app.yapam.file.model.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.net.URI;
import java.util.Objects;

public abstract class StorageProvider {

    private static Log log = LogFactory.getLog(StorageProvider.class);
    @Autowired private YapamProperties.StorageProvider.StorageProviderProperties storageProviderProperties;
    @Autowired private FileRepository fileRepository;

    @SuppressWarnings("squid:S00112")
    public abstract Boolean existsContent(String filepath) throws Exception;

    @SuppressWarnings("squid:S00112")
    public abstract void createDirectory(String path) throws Exception;

    private void createDirectories(String filepath) throws Exception {
        var uri = new URI(filepath).resolve(".");
        while (!uri.toString().equals(storageProviderProperties.getRootPath())) {
            createDirectory(uri.toString());
            uri = uri.resolve("..");
        }
    }

    private String getFilePath(String fileHash) {
        return storageProviderProperties.getRootPath() + fileHash;
    }

    @SuppressWarnings("squid:S00112")
    public abstract byte[] readContent(String filepath) throws Exception;

    public Resource readFile(String fileId) {
        var fileDao = fileRepository.findOneById(fileId);
        if (Objects.isNull(fileDao)) {
            throw new UnknownFileException();
        }
        try {
            var filePath = getFilePath(fileDao.getHash());
            var content = readContent(filePath);
            return new ByteArrayResource(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalErrorException("Unable to read the file content");
        }
    }

    @SuppressWarnings("squid:S00112")
    public abstract void storeContent(byte[] content, String filepath) throws Exception;

    public void storeFile(File file, String fileId) {
        var fileDao = fileRepository.findOneById(fileId);
        if (Objects.isNull(fileDao)) {
            throw new UnknownFileException();
        }

        try {
            var filePath = getFilePath(fileDao.getHash());
            if (!existsContent(filePath)) {
                createDirectories(filePath);
                storeContent(file.getContent(), filePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalErrorException("Unable to write the file content");
        }
    }
}
