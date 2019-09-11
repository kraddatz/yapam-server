package app.yapam.common.service.storage_provider;

import app.yapam.common.service.StorageProvider;
import com.github.sardine.Sardine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "WEBDAV")
@Service
public class WebdavStorageProvider extends StorageProvider {

    @Autowired private Sardine sardine;

    @Override
    public void createDirectory(String path) throws Exception {
        sardine.createDirectory(path);
    }

    @Override
    public Boolean existsContent(String filepath) throws IOException {
        return sardine.exists(filepath);
    }

    @Override
    public byte[] readContent(String filepath) throws IOException {
        return sardine.get(filepath).readAllBytes();
    }

    @Override
    public void storeContent(byte[] content, String filepath) throws IOException {
        sardine.put(filepath, content);
    }
}
