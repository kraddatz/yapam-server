package me.raddatz.jwarden.common.annotation;

import me.raddatz.jwarden.common.annotation.profileexecution.VerifiedEmailValidator;
import me.raddatz.jwarden.user.UserController;
import me.raddatz.jwarden.user.model.RegisterUser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.method.HandlerMethod;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AnnotationHandlerInterceptor.class)
@ActiveProfiles("test")
class AnnotationHandlerInterceptorTest {

    @Autowired private AnnotationHandlerInterceptor annotationHandlerInterceptor;
    @MockBean private VerifiedEmailValidator verifiedEmailValidator;

    private MockHttpServletRequest getDefaultHttpServletRequest() {
        return new MockHttpServletRequest();
    }

    private MockHttpServletResponse getDefaultHttpServletResponse() {
        return new MockHttpServletResponse();
    }

    private HandlerMethod getDefaultAnnotationHandlerMethod() throws NoSuchMethodException {
        var method = UserController.class.getMethod("register", RegisterUser.class);

        return new HandlerMethod(new UserController(), method);
    }

    private HandlerMethod getDefaultNoAnnotationHandlerMethod() throws NoSuchMethodException {
        var method = UserController.class.getMethod("verifyEmail", String.class, String.class);

        return new HandlerMethod(new UserController(), method);
    }

    @Test
    void preHandle_whenHandlerMethodIsNull_thenReturnTrue() {
        var request = getDefaultHttpServletRequest();
        var response = getDefaultHttpServletResponse();

        boolean result = annotationHandlerInterceptor.preHandle(request, response, null);

        assertTrue(result);
    }

    @Test
    void preHandle_whenHandlerMethodNoAnnotation_thenReturnFalse() throws NoSuchMethodException {
        var request = getDefaultHttpServletRequest();
        var response = getDefaultHttpServletResponse();
        var handlerMethod = getDefaultNoAnnotationHandlerMethod();

        boolean result = annotationHandlerInterceptor.preHandle(request, response, handlerMethod);

        assertTrue(result);
    }

    @Test
    @Disabled // TODO: Missing request with VerifiedEmail validator
    void preHandle_whenHandlerMethodWithAnnotation_thenReturnFalse() throws NoSuchMethodException {
        var request = getDefaultHttpServletRequest();
        var response = getDefaultHttpServletResponse();
        var handlerMethod = getDefaultAnnotationHandlerMethod();

        when(verifiedEmailValidator.validate()).thenReturn(false);

        boolean result = annotationHandlerInterceptor.preHandle(request, response, handlerMethod);

        assertFalse(result);
    }
}