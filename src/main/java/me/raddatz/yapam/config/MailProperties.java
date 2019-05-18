package me.raddatz.yapam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

@Configuration
public class MailProperties {

    @Autowired private YapamProperties properties;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(sender);
        return sender;
    }

    private void applyProperties(JavaMailSenderImpl sender) {
        sender.setHost(this.properties.getMail().getHost());
        if (this.properties.getMail().getPort() != null) {
            sender.setPort(this.properties.getMail().getPort());
        }
        sender.setUsername(this.properties.getMail().getUsername());
        sender.setPassword(this.properties.getMail().getPassword());
        sender.setProtocol(this.properties.getMail().getProtocol());
        if (this.properties.getMail().getDefaultEncoding() != null) {
            sender.setDefaultEncoding(this.properties.getMail().getDefaultEncoding().name());
        }
        if (!this.properties.getMail().getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(this.properties.getMail().getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }
}
