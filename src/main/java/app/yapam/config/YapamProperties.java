package app.yapam.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "yapam")
public class YapamProperties {

    private String host;
    private Duration registrationTimeout;
    private YapamMail mail;
    private YapamDatasource datasource;
    private StorageProviderConfiguration storageProviderConfiguration;

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

    public enum StorageProviderType {
        FILESYSTEM,
        DROPBOX
    }

    @Getter
    @Setter
    @Component
    public static class StorageProviderConfiguration {
        private String rootPath = "/data";
        private StorageProviderType type;
        private DropboxStorageProviderConfiguration dropbox;
    }

    @Getter
    @Setter
    public static class DropboxStorageProviderConfiguration {

    }
}
