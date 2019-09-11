package app.yapam.common.service.storage_provider;

import app.yapam.common.service.StorageProvider;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "DROPBOX")
@Service
public class DropboxStorageProvider extends StorageProvider {

    @Autowired private DbxClientV2 client;

    @Override
    public void createDirectory(String path) {
        // not needed as uploadAndFinish in storeContent already creates the directories
    }

    @Override
    public Boolean existsContent(String filepath) throws Exception {
        try {
            client.files().getMetadata(filepath);
            return true;
        } catch (GetMetadataErrorException e) {
            if (e.getMessage().contains("{\".tag\":\"path\",\"path\":\"not_found\"}")) {
                return false;
            } else {
                throw e;
            }
        }
    }

    @Override
    public byte[] readContent(String filepath) throws Exception {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            client.files().downloadBuilder(filepath).download(os);
            return os.toByteArray();
        }
    }

    @Override
    public void storeContent(byte[] content, String filepath) throws Exception {
        try (InputStream in = new ByteArrayInputStream(content)) {
            client.files().uploadBuilder(filepath).uploadAndFinish(in);
        }
    }
}
