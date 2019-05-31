package app.yapam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatasourceProperties {

    @Autowired private YapamProperties yapamProperties;

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .password(this.yapamProperties.getDatasource().getPassword())
                .url(this.yapamProperties.getDatasource().getUrl())
                .username(this.yapamProperties.getDatasource().getUsername())
                .build();
    }
}
