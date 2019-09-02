package app.yapam.config;

import lombok.Getter;
import lombok.Setter;
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
    private YapamMail mail;
    private YapamDatasource datasource;
    private StorageProviderProperties storageProvider;

    public enum StorageProviderType {
        FILESYSTEM,
        DROPBOX
    }

    @Getter
    @Setter
    public static class YapamMail {

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
    public static class YapamDatasource {
        private String url;
    }

    @Getter
    @Setter
    @Component
    public static class StorageProviderProperties {
        private String rootPath;
        private StorageProviderType type;
        private DropboxStorageProviderConfiguration dropbox;

        @Getter
        @Setter
        @Component
        public static class DropboxStorageProviderConfiguration {
            private String accessToken;
        }
    }
}
