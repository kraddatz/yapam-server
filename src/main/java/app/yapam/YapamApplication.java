package app.yapam;

import app.yapam.config.AppParameter;
import app.yapam.config.YapamProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties({AppParameter.class, YapamProperties.class})
public class YapamApplication {

    public static void main(String[] args) {
        SpringApplication.run(YapamApplication.class, args);
    }
}
