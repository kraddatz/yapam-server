package me.raddatz.jwarden.config.oauth;

import me.raddatz.jwarden.common.service.PBKDF2Service;
import me.raddatz.jwarden.user.repository.UserRepository;
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

    @Autowired private PBKDF2Service pbkdf2Service;

    @Override
    public Authentication authenticate(Authentication authentication) {
        var email = authentication.getName();
        var user = userRepository.findOneByEmail(email);
        if (Objects.isNull(user)) {
            throw new BadCredentialsException("Invalid username or password");
        }

        var password = authentication.getCredentials().toString();
        var salt = user.getMasterPasswordSalt();
        if (pbkdf2Service.equals(password, salt, user)) {
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
