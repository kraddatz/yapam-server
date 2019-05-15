package me.raddatz.yapam.common.annotation;

import me.raddatz.yapam.common.annotation.userexists.UserExists;
import me.raddatz.yapam.common.annotation.userexists.UserExistsValidator;
import me.raddatz.yapam.common.annotation.verifiedemail.VerifiedEmail;
import me.raddatz.yapam.common.annotation.verifiedemail.VerifiedEmailValidator;
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
    @Autowired private UserExistsValidator userExistsValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        Method method = ((HandlerMethod) handler).getMethod();
        if (method.isAnnotationPresent(VerifiedEmail.class)) {
            return verifiedEmailValidator.validate();
        }
        if (method.isAnnotationPresent(UserExists.class)) {
            return userExistsValidator.validate(request);
        }
        return true;
    }
}
