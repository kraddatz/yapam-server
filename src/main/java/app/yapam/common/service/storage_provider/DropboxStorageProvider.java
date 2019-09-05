package app.yapam.common.service.storage_provider;

import app.yapam.common.service.StorageProvider;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Path;

@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "DROPBOX")
@Service
public class DropboxStorageProvider extends StorageProvider {

    @Autowired private DbxClientV2 client;

    @Override
    public byte[] readContent(Path filepath) throws Exception {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            client.files().downloadBuilder(filepath.toString()).download(os);
            return os.toByteArray();
        }
    }

    @Override
    public void storeContent(byte[] content, Path filepath) throws Exception {
        try (InputStream in = new ByteArrayInputStream(content)) {
            client.files().uploadBuilder(filepath.toString()).uploadAndFinish(in);
        }
    }
}
