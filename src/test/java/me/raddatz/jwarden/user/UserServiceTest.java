package me.raddatz.jwarden.user;

import me.raddatz.jwarden.common.annotation.AnnotationHandlerInterceptor;
import me.raddatz.jwarden.common.error.EmailAlreadyExistsException;
import me.raddatz.jwarden.common.error.InvalidEmailVerificationTokenException;
import me.raddatz.jwarden.common.error.UserNotExistsException;
import me.raddatz.jwarden.common.service.EmailService;
import me.raddatz.jwarden.common.service.PBKDF2Service;
import me.raddatz.jwarden.user.model.RegisterUser;
import me.raddatz.jwarden.user.model.User;
import me.raddatz.jwarden.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {UserService.class})
class UserServiceTest {

    @Autowired private UserService userService;
    @MockBean private PBKDF2Service pbkdf2Service;
    @MockBean private UserRepository userRepository;
    @MockBean private EmailService emailService;
    @MockBean private AnnotationHandlerInterceptor annotationHandlerInterceptor;

    private RegisterUser createDefaultRegisterUser() {
        var user = new RegisterUser();
        user.setName("Name");
        user.setEmail("email@test.com");
        user.setMasterPassword("password");
        return user;
    }

    private User creteDefaultUser() {
        var user = new User();
        user.setEmailToken("token");
        return user;
    }

    @Test
    void register_whenUserNotExists_thenRegisterUser() {
        when(pbkdf2Service.generateSalt()).thenReturn("salt");
        when(pbkdf2Service.generateSaltedHash(Mockito.anyString(), Mockito.anyString())).thenReturn("saltedhash");
        var registerUser = createDefaultRegisterUser();

        userService.register(registerUser);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        var user = captor.getValue();

        assertEquals(registerUser.getEmail(), user.getEmail());
        assertEquals(registerUser.getName(), user.getName());
        assertEquals("saltedhash", user.getMasterPasswordHash());
        assertEquals("salt", user.getMasterPasswordSalt());
    }

    @Test
    void register_whenUserExists_thenThrowException() {
        when(pbkdf2Service.generateSalt()).thenReturn("salt");
        when(pbkdf2Service.generateSaltedHash(Mockito.anyString(), Mockito.anyString())).thenReturn("saltedhash");
        when(userRepository.findOneByEmail(Mockito.anyString())).thenReturn(new User());
        var registerUser = createDefaultRegisterUser();

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(registerUser));
    }

    @Test
    void verifyEmail_whenUserNotExists_doNothing() {
        var userId = "userid";
        var token = "token";
        when(userRepository.findOneById(anyString())).thenReturn(null);

        assertThrows(UserNotExistsException.class, () -> userService.verifyEmail(userId, token));
    }

    @Test
    void verifyEmail_whenUserExists_saveUser() {
        var userId = "userid";
        var token = "token";
        var user = creteDefaultUser();
        when(userRepository.findOneById(anyString())).thenReturn(user);

        userService.verifyEmail(userId, token);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void verifyEmail_whenUserExistsAndInvalidToken_throwException() {
        var userId = "userid";
        var token = "invalidtoken";
        var user = creteDefaultUser();
        when(userRepository.findOneById(anyString())).thenReturn(user);

        assertThrows(InvalidEmailVerificationTokenException.class, () -> userService.verifyEmail(userId, token));
    }
}