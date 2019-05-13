package me.raddatz.jwarden.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.raddatz.jwarden.common.error.UnauthorizedUserException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class RequestHelperService {

    private HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    private String getAuthenticationHeader() {
        var request = getCurrentHttpRequest();

        if (request != null) {
            return request.getHeader("Authorization");
        }
        return "";
    }

    private String getPropertyFromAT(String property) {
        var objectMapper = new ObjectMapper();
        String rawHeader = getAuthenticationHeader();
        String token = rawHeader.replace("Bearer ", "");
        Jwt jwt = JwtHelper.decode(token);
        Map claims;
        try {
            claims = objectMapper.readValue(jwt.getClaims(), Map.class);
        } catch (IOException e) {
            return null;
        }
        try {
            return claims.get(property).toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getUserName() {
        var userId = getPropertyFromAT("user_name");
        if (Objects.isNull(userId)) {
            throw new UnauthorizedUserException();
        }
        return userId;
    }
}
