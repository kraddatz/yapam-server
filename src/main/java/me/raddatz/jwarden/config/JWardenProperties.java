package me.raddatz.jwarden.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwarden")
public class JWardenProperties {

    private Duration registrationTimeout;

    private Security security;

    @Getter
    @Setter
    public static class Security {
        private Oauth oauth;

        @Getter
        @Setter
        public static class Oauth {
            private Duration accessTokenValidityPeriod;
            private Duration refreshTokenValidityPeriod;
        }
    }


}
