package app.yapam.common.service;

import app.yapam.common.error.InternalErrorException;
import app.yapam.common.repository.FileRepository;
import app.yapam.config.YapamProperties;
import app.yapam.file.model.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public abstract class StorageProvider {

    private static Log log = LogFactory.getLog(StorageProvider.class);
    @Autowired private YapamProperties.StorageProvider.StorageProviderProperties storageProviderProperties;
    @Autowired private FileRepository fileRepository;

    @SuppressWarnings("squid:S00112")
    public abstract Boolean existsContent(String filepath) throws Exception;

    private String getFilePath(String fileHash) {
        var filepath = storageProviderProperties.getRootPath();
        if (!filepath.endsWith(java.io.File.separator)) {
            filepath += java.io.File.separator;
        }
        return filepath + fileHash;
    }

    @SuppressWarnings("squid:S00112")
    public abstract byte[] readContent(String filepath) throws Exception;

    public Resource readFile(String fileId) {
        try {
            var fileDao = fileRepository.findOneById(fileId);
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
        try {
            var fileDao = fileRepository.findOneById(fileId);
            var filePath = getFilePath(fileDao.getHash());
            if (!existsContent(filePath)) {
                storeContent(file.getContent(), filePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalErrorException("Unable to write the file content");
        }
    }
}
