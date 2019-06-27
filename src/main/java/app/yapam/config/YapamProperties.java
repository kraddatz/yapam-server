package app.yapam.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "yapam")
public class YapamProperties {

    private Duration registrationTimeout;
    private Security security;
    private Mail mail;
    private Datasource datasource;

    @Getter
    @Setter
    public static class Security {
        private Integer bcryptIterations = 10;
        private Oauth oauth;

        @Getter
        @Setter
        public static class Oauth {
            private Duration accessTokenValidityPeriod;
            private Duration refreshTokenValidityPeriod;
        }
    }

    @Getter
    @Setter
    public static class Mail {

        private String host;
        private Integer port;
        private String username;
        private String password;
        private String protocol = "smtp";
        private Charset defaultEncoding = StandardCharsets.UTF_8;
        private Map<String, String> properties = new HashMap<>();
        private String jndiName;
        private String messageSender;
    }

    @Getter
    @Setter
    public static class Datasource {
        private String url;
    }
}
