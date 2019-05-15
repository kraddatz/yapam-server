package me.raddatz.yapam.config.oauth;

import me.raddatz.yapam.common.service.BCryptService;
import me.raddatz.yapam.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Objects;

@Configuration
public class AuthenticationProviderConfig implements AuthenticationProvider {

    @Autowired private UserRepository userRepository;

    @Autowired private BCryptService bcryptService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        var email = authentication.getName();
        var user = userRepository.findOneByEmail(email);
        if (Objects.isNull(user)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var password = authentication.getCredentials().toString();
        if (bcryptService.checkHash(password, user)) {
            return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
