package app.yapam.common.service.storage_provider;

import app.yapam.common.service.StorageProvider;
import com.github.sardine.Sardine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "WEBDAV")
@Service
public class WebdavStorageProvider extends StorageProvider {

    @Autowired private Sardine sardine;

    @Override
    public Boolean existsContent(String filepath) throws IOException {
        return sardine.exists(filepath);
    }

    @Override
    public byte[] readContent(String filepath) throws IOException {
        return sardine.get(filepath).readAllBytes();
    }

    @Override
    public void storeContent(byte[] content, String filepath) throws IOException, URISyntaxException {
        var parentPath = new URI(filepath).resolve(".").toString();
        sardine.createDirectory(parentPath);
        sardine.put(filepath, content);
    }
}
