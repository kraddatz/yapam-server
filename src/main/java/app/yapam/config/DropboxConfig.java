package app.yapam.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "DROPBOX")
public class DropboxConfig {

    @Autowired private YapamProperties yapamProperties;

    @Bean
    public DbxClientV2 dbxClientV2() {
        System.out.println("XXXXXX");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("yapam-asdf").build();
        return new DbxClientV2(config, yapamProperties.getStorageProvider().getDropbox().getAccessToken());
    }
}
