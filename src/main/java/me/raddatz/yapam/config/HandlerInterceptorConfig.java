package me.raddatz.yapam.config;

import me.raddatz.yapam.common.annotation.AnnotationHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Profile("local")
public class HandlerInterceptorConfig implements WebMvcConfigurer {

    @Autowired private AnnotationHandlerInterceptor annotationHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(annotationHandlerInterceptor);
    }
}
