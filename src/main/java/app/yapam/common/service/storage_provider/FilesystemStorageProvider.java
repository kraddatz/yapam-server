package app.yapam.common.service.storage_provider;

import app.yapam.common.service.StorageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "FILESYSTEM")
@Service
public class FilesystemStorageProvider extends StorageProvider {

    @Override
    public byte[] readContent(String filepath) throws IOException {
        return Files.readAllBytes(Paths.get(filepath));
    }

    @Override
    public void storeContent(byte[] content, String filepath) throws IOException {
        var path = Paths.get(filepath);
        Files.createDirectories(path.getParent());
        Files.write(path, content);
    }

    @Override
    public Boolean existsContent(String filepath) {
        var path = Paths.get(filepath);
        return Files.exists(path);
    }
}
