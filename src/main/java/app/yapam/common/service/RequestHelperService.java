package app.yapam.common.service;

import app.yapam.common.error.InvalidAccessTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class RequestHelperService {

    private String getAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization").replace("Bearer ", "");
    }

    private String getPropertyFromAT(String property) {
        var objectMapper = new ObjectMapper();
        try {
            Jwt jwt = JwtHelper.decode(getAccessToken());
            Map claims = objectMapper.readValue(jwt.getClaims(), Map.class);
            return claims.get(property).toString();
        } catch (IOException | NullPointerException e) {
            log.error("Access token is not a JSON web token");
            throw new InvalidAccessTokenException();
        }
    }

    public String getEmail() {
        return getPropertyFromAT("email");
    }
}
