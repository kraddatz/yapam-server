package app.yapam.file;

import app.yapam.common.error.UnknownFileException;
import app.yapam.common.repository.FileDao;
import app.yapam.common.repository.FileRepository;
import app.yapam.common.repository.SecretDao;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.StorageProvider;
import app.yapam.file.model.File;
import app.yapam.file.model.response.SimpleFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileService {

    @Autowired private MappingService mappingService;
    @Autowired private StorageProvider storageProvider;
    @Autowired private FileRepository fileRepository;

    public void attachSecretToFiles(List<File> files, SecretDao secretDao) {
        List<FileDao> fileDaos = new ArrayList<>();
        for (File file : files) {
            var fileDao = fileRepository.findOneById(file.getId());
            if (Objects.isNull(fileDao)) {
                throw new UnknownFileException(file.getId());
            }
            fileDao.getSecrets().add(secretDao);
            fileDaos.add(fileDao);
        }
        fileRepository.saveAll(fileDaos);
    }

    @PreAuthorize("@permissionEvaluator.hasAccessToFile(#fileId, 'READ')")
    Resource getFileForId(String fileId) {
        return storageProvider.readFile(fileId);
    }

    @PreAuthorize("@permissionEvaluator.registeredUser()")
    public SimpleFileResponse saveFile(MultipartFile fileRequest) {
        var file = mappingService.fileFromRequest(fileRequest);
        var fileDao = fileRepository.findOneByHash(file.getHash());
        if (Objects.isNull(fileDao)) {
            fileDao = fileRepository.save(mappingService.fileToDao(file));
            storageProvider.storeFile(file, fileDao.getId());
        }
        return mappingService.fileDaoToSimpleResponse(fileDao);
    }
}
