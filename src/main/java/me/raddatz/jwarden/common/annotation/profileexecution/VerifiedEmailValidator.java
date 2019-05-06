package me.raddatz.jwarden.common.annotation.profileexecution;

import me.raddatz.jwarden.common.error.EmailNotVerifiedException;
import org.springframework.stereotype.Component;

@Component
public class VerifiedEmailValidator {

    public boolean validate() {
        throw new EmailNotVerifiedException();
    }
}
