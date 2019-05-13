package me.raddatz.jwarden.common.annotation.verifiedemail;

import me.raddatz.jwarden.common.error.EmailNotVerifiedException;
import me.raddatz.jwarden.common.error.UserNotFoundException;
import me.raddatz.jwarden.common.service.RequestHelperService;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VerifiedEmailValidator {

    @Autowired private RequestHelperService requestHelperService;
    @Autowired private UserRepository userRepository;

    public boolean validate() {
        var user = userRepository.findOneByEmail(requestHelperService.getUserName());
        if (Objects.isNull(user)) {
            throw new UserNotFoundException();
        }
        if (!user.getEmailVerified()) {
            throw new EmailNotVerifiedException();
        }
        return true;
    }
}
