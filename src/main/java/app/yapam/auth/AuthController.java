package app.yapam.auth;

import app.yapam.auth.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired private AuthService authService;

    @GetMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Auth getAuth() {
        return authService.getAuth();
    }
}
