package me.raddatz.jwarden.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;

@Configuration
public class PBDKF2Config {

    @Bean
    public SecretKeyFactory secretKeyFactory() throws NoSuchAlgorithmException {
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    }
}
