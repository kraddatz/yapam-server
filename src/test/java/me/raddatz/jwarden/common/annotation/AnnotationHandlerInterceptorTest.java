package me.raddatz.jwarden.common.annotation;

import me.raddatz.jwarden.common.annotation.userexists.UserExistsValidator;
import me.raddatz.jwarden.common.annotation.verifiedemail.VerifiedEmailValidator;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AnnotationHandlerInterceptor.class)
@ActiveProfiles("test")
class AnnotationHandlerInterceptorTest {

    @Autowired private AnnotationHandlerInterceptor annotationHandlerInterceptor;
    @MockBean private VerifiedEmailValidator verifiedEmailValidator;
    @MockBean private UserExistsValidator userExistsValidator;

    private MockHttpServletRequest createDefaultHttpServletRequest() {
        return new MockHttpServletRequest();
    }

    private MockHttpServletResponse createDefaultHttpServletResponse() {
        return new MockHttpServletResponse();
    }

    private HandlerMethod createNoAnnotationHandlerMethod() throws NoSuchMethodException {
        var method = UserController.class.getMethod("register", RegisterUser.class);

        return new HandlerMethod(new UserController(), method);
    }

    private HandlerMethod createVerifiedEmailHandlerMethod() throws NoSuchMethodException {
        var method = UserController.class.getMethod("register", RegisterUser.class);

        return new HandlerMethod(new UserController(), method);
    }

    private HandlerMethod createUserExistsHandlerMethod() throws NoSuchMethodException {
        var method = UserController.class.getMethod("verifyEmail", String.class, String.class);

        return new HandlerMethod(new UserController(), method);
    }

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
    @Disabled // TODO: Missing request with VerifiedEmail validator
    void preHandleVerifiedEmail_when_thenReturnFalse() throws NoSuchMethodException {
        var request = createDefaultHttpServletRequest();
        var response = createDefaultHttpServletResponse();
        var handlerMethod = createVerifiedEmailHandlerMethod();

        when(verifiedEmailValidator.validate()).thenReturn(false);

        boolean result = annotationHandlerInterceptor.preHandle(request, response, handlerMethod);

        assertFalse(result);
    }

    @Test
    void preHandleUserExists_whenUserNotExists_thenReturnFalse() throws NoSuchMethodException {
        var request = createDefaultHttpServletRequest();
        var response = createDefaultHttpServletResponse();
        var handlerMethod = createUserExistsHandlerMethod();

        when(userExistsValidator.validate(request)).thenReturn(false);

        boolean result = annotationHandlerInterceptor.preHandle(request, response, handlerMethod);

        assertFalse(result);
    }
}