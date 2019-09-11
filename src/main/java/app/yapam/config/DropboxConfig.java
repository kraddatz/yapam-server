package app.yapam.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "DROPBOX")
public class DropboxConfig {

    @Bean
    public DbxClientV2 dbxClientV2(YapamProperties.StorageProvider.DropboxStorageProviderProperties storageProviderProperties) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder(storageProviderProperties.getClientIdentifier()).build();
        return new DbxClientV2(config, storageProviderProperties.getAccessToken());
    }
}
