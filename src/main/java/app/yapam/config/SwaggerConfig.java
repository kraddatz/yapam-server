package app.yapam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Profile("!test")
public class SwaggerConfig implements WebMvcConfigurer {

    @Autowired private BuildProperties buildProperties;

    @Bean
    public Docket apiDocket(YapamProperties yapamProperties, AppParameter appParameter) {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(appParameter.getSwaggerSchemes())
                .select()
                .apis(RequestHandlerSelectors.basePackage("app.yapam"))
                .paths(PathSelectors.any())
                .build()
                .host(yapamProperties.getHost())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("yapam-server")
                .description("yapam-server is the server component of the yapam environment. It manages the users and secrets.")
                .version(buildProperties.getVersion())
                .build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.LIST) // or DocExpansion.NONE or DocExpansion.FULL
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/documentation/**").addResourceLocations("classpath:/META-INF/resources/");
    }


    private List<? extends SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header"));
    }

    private List<SecurityContext> securityContexts() {
        return Collections.singletonList(SecurityContext.builder().forPaths(PathSelectors.any()).securityReferences(securityReferences()).build());
    }

    private List<SecurityReference> securityReferences() {
        return Collections.singletonList(SecurityReference.builder().reference(HttpHeaders.AUTHORIZATION).scopes(new AuthorizationScope[0]).build());
    }

}