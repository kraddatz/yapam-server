package app.yapam.common.service.storage_provider;

import app.yapam.common.service.StorageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "FILESYSTEM")
@Service
public class FilesystemStorageProvider extends StorageProvider {

    @Override
    public byte[] readContent(Path filepath) throws IOException {
        return Files.readAllBytes(filepath);
    }

    @Override
    public void storeContent(byte[] content, Path filepath) throws IOException {
        Files.createDirectories(filepath.getParent());
        Files.write(filepath, content);
    }
}
