package app.yapam.common.annotation;

import app.yapam.common.annotation.verifiedemail.VerifiedEmail;
import app.yapam.common.annotation.verifiedemail.VerifiedEmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AnnotationHandlerInterceptor implements HandlerInterceptor {

    @Autowired private VerifiedEmailValidator verifiedEmailValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Method method = ((HandlerMethod) handler).getMethod();
        if (method.isAnnotationPresent(VerifiedEmail.class)) {
            return verifiedEmailValidator.validate();
        }
        return true;
    }
}
