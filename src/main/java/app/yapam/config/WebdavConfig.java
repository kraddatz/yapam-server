package app.yapam.config;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "WEBDAV")
public class WebdavConfig {

    @Bean
    public Sardine sardine(YapamProperties.StorageProvider.WebdavStorageProviderProperties storageProviderProperties) {
        return SardineFactory.begin(storageProviderProperties.getUsername(), storageProviderProperties.getPassword());
    }
}
