package me.raddatz.jwarden.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppParameter {

    private String buildTime;

    private String appName;

    private String javaVersion;

    private String springBootVersion;

    private String tomcatVersion;

    private String host;

}