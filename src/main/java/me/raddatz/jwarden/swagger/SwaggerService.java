package me.raddatz.jwarden.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

@Service
@Slf4j
public class SwaggerService {

    @Autowired private DocumentationCache documentationCache;
    @Autowired private ServiceModelToSwagger2Mapper mapper;

    @Qualifier("yamlObjectMapper")
    @Autowired private ObjectMapper yamlObjectMapper;

    private Swagger getSwaggerObject() {
        Documentation documentation = documentationCache.documentationByGroup("default");
        if (documentation == null) {
            return null;
        }

        return mapper.mapDocumentation(documentation);
    }

    String getSwaggerYamlSpec() {
        try {
            return yamlObjectMapper.writeValueAsString(getSwaggerObject());
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
