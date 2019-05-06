package me.raddatz.jwarden.config;

import feign.Logger.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Level feignLoggerLevel() {
        return Level.FULL;
    }

}