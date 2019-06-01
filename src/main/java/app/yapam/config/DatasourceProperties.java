package app.yapam.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatasourceProperties {

    @Bean
    @Primary
    public DataSource dataSource(YapamProperties yapamProperties) {
        return DataSourceBuilder.create()
                .password(yapamProperties.getDatasource().getPassword())
                .url(yapamProperties.getDatasource().getUrl())
                .username(yapamProperties.getDatasource().getUsername())
                .build();
    }
}
