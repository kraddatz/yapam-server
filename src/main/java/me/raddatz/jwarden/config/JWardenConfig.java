package me.raddatz.jwarden.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwarden")
public class JWardenConfig {

    private Duration registrationTimeout;
}
