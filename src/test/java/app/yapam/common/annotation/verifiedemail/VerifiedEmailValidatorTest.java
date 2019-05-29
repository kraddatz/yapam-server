package app.yapam.common.annotation.verifiedemail;

import app.yapam.common.error.EmailNotVerifiedException;
import app.yapam.common.service.RequestHelperService;
import app.yapam.common.error.UserNotFoundException;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(VerifiedEmailValidator.class)
@ActiveProfiles("test")
class VerifiedEmailValidatorTest {

    @Autowired private VerifiedEmailValidator verifiedEmailValidator;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private UserRepository userRepository;

    private UserDBO createDefaultUser() {
        var user = new UserDBO();
        user.setEmailVerified(true);
        return user;
    }

    @Test
    void validate_whenUserExists_thenReturnTrue() {
        var user = createDefaultUser();
        when(requestHelperService.getUserName()).thenReturn("userSecrets@email.com");
        when(userRepository.findOneByEmail("userSecrets@email.com")).thenReturn(user);

        var result = verifiedEmailValidator.validate();

        Assertions.assertTrue(result);
    }

    @Test
    void validate_whenUserNotExists_thenThrowException() {
        when(requestHelperService.getUserName()).thenReturn("invaliduser@email.com");
        when(userRepository.findOneByEmail("invaliduser@email.com")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> verifiedEmailValidator.validate());
    }

    @Test
    void validate_whenUserEmailNotVerified_thenThrowException() {
        var user = createDefaultUser();
        user.setEmailVerified(false);
        when(requestHelperService.getUserName()).thenReturn("invaliduser@email.com");
        when(userRepository.findOneByEmail("invaliduser@email.com")).thenReturn(user);

        assertThrows(EmailNotVerifiedException.class, () -> verifiedEmailValidator.validate());
    }
}