package me.raddatz.jwarden.common.annotation.userexists;

import me.raddatz.jwarden.common.error.UserNotFoundException;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserExistsValidator.class)
@ActiveProfiles("test")
class UserExistsValidatorTest {

    @Autowired private UserExistsValidator userExistsValidator;
    @MockBean private UserRepository userRepository;

    private MockHttpServletRequest createDefaultHttpServletRequest() {
        var request = new MockHttpServletRequest();
        request.setContextPath("/users/e515ab50-1328-446b-aabf-6f565b266f41/email/verify");
        return request;
    }

    private User createDefaultUser() {
        return new User();
    }

    @Test
    void validate_whenUserExists_thenReturnTrue() {
        var request = createDefaultHttpServletRequest();
        var user = createDefaultUser();
        when(userRepository.findOneById("e515ab50-1328-446b-aabf-6f565b266f41")).thenReturn(user);

        boolean result = userExistsValidator.validate(request);

        assertTrue(result);
    }

    @Test
    void validate_whenUserNotExists_thenReturnTrue() {
        var request = createDefaultHttpServletRequest();
        var user = createDefaultUser();
        when(userRepository.findOneById("e515ab50-1328-446b-aabf-6f565b266f41")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userExistsValidator.validate(request));
    }
}