package me.raddatz.yapam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;
import java.util.Properties;

@Configuration
public class MailProperties {

    @Autowired private YapamProperties yapamProperties;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(sender);
        return sender;
    }

    private void applyProperties(JavaMailSenderImpl sender) {
        sender.setHost(this.yapamProperties.getMail().getHost());
        if (this.yapamProperties.getMail().getPort() != null) {
            sender.setPort(this.yapamProperties.getMail().getPort());
        }
        sender.setUsername(this.yapamProperties.getMail().getUsername());
        sender.setPassword(this.yapamProperties.getMail().getPassword());
        sender.setProtocol(this.yapamProperties.getMail().getProtocol());
        if (this.yapamProperties.getMail().getDefaultEncoding() != null) {
            sender.setDefaultEncoding(this.yapamProperties.getMail().getDefaultEncoding().name());
        }
        if (!this.yapamProperties.getMail().getProperties().isEmpty()) {
            sender.setJavaMailProperties(asProperties(this.yapamProperties.getMail().getProperties()));
        }
    }

    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }
}
