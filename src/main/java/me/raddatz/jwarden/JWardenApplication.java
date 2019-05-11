package me.raddatz.jwarden;

import me.raddatz.jwarden.config.AppParameter;
import me.raddatz.jwarden.config.JWardenConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({AppParameter.class, JWardenConfig.class})
public class JWardenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JWardenApplication.class, args);
    }
}
