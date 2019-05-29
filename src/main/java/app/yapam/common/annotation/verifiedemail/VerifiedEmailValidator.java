package app.yapam.common.annotation.verifiedemail;

import app.yapam.common.error.EmailNotVerifiedException;
import app.yapam.common.error.UserNotFoundException;
import app.yapam.common.service.RequestHelperService;
import app.yapam.user.repository.UserRepository;
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
