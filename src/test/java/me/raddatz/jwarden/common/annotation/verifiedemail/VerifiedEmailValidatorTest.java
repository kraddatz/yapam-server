package me.raddatz.jwarden.common.annotation.verifiedemail;

import me.raddatz.jwarden.common.error.EmailNotVerifiedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VerifiedEmailValidator.class)
@ActiveProfiles("test")
class VerifiedEmailValidatorTest {

    @Autowired private VerifiedEmailValidator verifiedEmailValidator;

    @Test
    void validate_throwException() {
        Assertions.assertThrows(EmailNotVerifiedException.class, () -> verifiedEmailValidator.validate());
    }
}