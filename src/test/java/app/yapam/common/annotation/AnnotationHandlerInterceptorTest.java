package app.yapam.common.annotation;

import app.yapam.YapamBaseTest;
import app.yapam.common.annotation.verifiedemail.VerifiedEmailValidator;
import app.yapam.secret.SecretController;
import app.yapam.secret.model.request.SecretRequest;
import app.yapam.user.UserController;
import app.yapam.user.model.request.UserRequest;
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
class AnnotationHandlerInterceptorTest extends YapamBaseTest {

    @Autowired private AnnotationHandlerInterceptor annotationHandlerInterceptor;
    @MockBean private VerifiedEmailValidator verifiedEmailValidator;

    @Test
    void preHandle_whenHandlerMethodIsNull_thenReturnTrue() {
        var request = createDefaultHttpServletRequest();
        var response = createDefaultHttpServletResponse();

        boolean result = annotationHandlerInterceptor.preHandle(request, response, null);

        assertTrue(result);
    }

    @Test
    void preHandle_whenHandlerMethodNoAnnotation_thenReturnTrue() throws NoSuchMethodException {
        var request = createDefaultHttpServletRequest();
        var response = createDefaultHttpServletResponse();
        var handlerMethod = createNoAnnotationHandlerMethod();

        boolean result = annotationHandlerInterceptor.preHandle(request, response, handlerMethod);

        assertTrue(result);
    }

    @Test
    void preHandleVerifiedEmail_when_thenReturnFalse() throws NoSuchMethodException {
        var request = createDefaultHttpServletRequest();
        var response = createDefaultHttpServletResponse();
        var handlerMethod = createVerifiedEmailHandlerMethod();

        when(verifiedEmailValidator.validate()).thenReturn(false);

        boolean result = annotationHandlerInterceptor.preHandle(request, response, handlerMethod);

        assertFalse(result);
    }
}