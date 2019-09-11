package app.yapam.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "yapam")
public class YapamProperties {

    private String host;
    private MailProperties mail;
    private DatasourceProperties datasource;
    private StorageProvider storageProvider;
    private IdentityProviderType identityProvider;

    public enum IdentityProviderType {
        KEYCLOAK
    }

    public enum StorageProviderType {
        FILESYSTEM,
        DROPBOX,
        WEBDAV
    }

    @Getter
    @Setter
    @Component
    @ConfigurationProperties(prefix = "yapam.mail")
    public static class MailProperties {

        private String host;
        private Integer port;
        private String username;
        private String password;
        private String protocol = "smtp";
        private Charset defaultEncoding = StandardCharsets.UTF_8;
        private Map<String, String> properties = new HashMap<>();
        private String jndiName;
        private Boolean testConnection;
        private String messageSender;
    }

    @Getter
    @Setter
    public static class DatasourceProperties {
        private String url;
    }

    @Getter
    @Setter
    @Component
    public static class StorageProvider {
        private StorageProviderType type;
        private FilesystemStorageProviderProperties filesystem;
        private DropboxStorageProviderProperties dropbox;

        public interface StorageProviderProperties {
            String getRootPath();
        }

        @Getter
        @Setter
        @Component("storageProviderProperties")
        @ConfigurationProperties(prefix = "yapam.storage-provider.filesystem")
        @ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "FILESYSTEM")
        public static class FilesystemStorageProviderProperties implements StorageProviderProperties {
            private String rootPath;
        }

        @Getter
        @Setter
        @Component("storageProviderProperties")
        @ConfigurationProperties(prefix = "yapam.storage-provider.dropbox")
        @ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "DROPBOX")
        public static class DropboxStorageProviderProperties implements StorageProviderProperties {
            private String rootPath;
            private String accessToken;
        }

        @Getter
        @Setter
        @Component("storageProviderProperties")
        @ConfigurationProperties(prefix = "yapam.storage-provider.webdav")
        @ConditionalOnProperty(name = "yapam.storage-provider.type", havingValue = "WEBDAV")
        public static class WebdavStorageProviderProperties implements StorageProviderProperties {
            private String rootPath;
            private String username;
            private String password;
        }
    }

}
