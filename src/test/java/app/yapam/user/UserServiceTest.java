package app.yapam.user;

import app.yapam.YapamBaseTest;
import app.yapam.common.error.EmailAlreadyExistsException;
import app.yapam.common.error.EmailVerificationTokenExpiredException;
import app.yapam.common.error.InvalidEmailVerificationTokenException;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.config.YapamProperties;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import app.yapam.user.repository.UserTransactions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@ActiveProfiles("test")
class UserServiceTest extends YapamBaseTest {

    @Autowired private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private EmailService emailService;
    @MockBean private YapamProperties yapamProperties;
    @MockBean private UserTransactions userTransactions;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private MappingService mappingService;

    @Test
    void register_whenUserNotExists_thenRegisterUser() {
        var registerUser = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(null);
        when(mappingService.copyUserRequestToDBO(any(UserRequest.class), any(UserDBO.class))).thenReturn(userDBO);

        userService.createUser(registerUser);

        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);
        verify(userTransactions).tryToCreateUser(captor.capture());
        userDBO = captor.getValue();

        assertEquals(registerUser.getEmail(), userDBO.getEmail());
        assertEquals(registerUser.getName(), userDBO.getName());
        assertEquals(registerUser.getMasterPasswordHash(), userDBO.getMasterPasswordHash());
    }

    @Test
    void register_whenUserExistsInRegistrationUnverified_thenOverrideUser() {
        var registerUser = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        userDBO.setEmailVerified(false);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(mappingService.copyUserRequestToDBO(any(UserRequest.class), any(UserDBO.class))).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));

        userService.createUser(registerUser);

        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);
        verify(userTransactions).tryToCreateUser(captor.capture());
        userDBO = captor.getValue();

        assertEquals(registerUser.getEmail(), userDBO.getEmail());
        assertEquals(registerUser.getName(), userDBO.getName());
        assertEquals(registerUser.getMasterPasswordHash(), userDBO.getMasterPasswordHash());
    }

    @Test
    void register_whenUserExistsNotInRegistrationUnverified_thenThrowException() {
        var userRequest = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        userDBO.setEmailVerified(false);
        userDBO.setCreationDate(LocalDateTime.now().minusDays(2));
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void register_whenUserExistsNotInRegistrationVerified_thenThrowException() {
        var userRequest = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        userDBO.setEmailVerified(true);
        userDBO.setCreationDate(LocalDateTime.now().minusDays(2));
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void register_whenUserExistsInRegistrationVerified_thenThrowException() {
        var userRequest = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        userDBO.setEmailVerified(true);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void verifyEmail_validToken_thenSaveUser() {
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        userService.verifyEmail(DEFAULT_USER_ID, DEFAULT_USER_EMAIL_TOKEN);
        verify(userRepository, times(1)).save(any(UserDBO.class));
    }

    @Test
    void verifyEmail_whenInvalidToken_thenThrowException() {
        var token = "invalidtoken";
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        assertThrows(InvalidEmailVerificationTokenException.class, () -> userService.verifyEmail(DEFAULT_USER_ID, token));
    }

    @Test
    void verifyEmail_whenUserNotInRegistrationPeriod_thenThrowException() {
        var token = "invalidtoken";
        var userDBO = createDefaultUserDBO();
        userDBO.setCreationDate(LocalDateTime.now().minusDays(2));
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        assertThrows(EmailVerificationTokenExpiredException.class, () -> userService.verifyEmail(DEFAULT_USER_ID, token));
    }

    @Test
    void requestEmailChange_whenUserExists_thenRequestEmailChange() {
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(mappingService.userFromDBO(userDBO)).thenReturn(user);
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        userService.requestEmailChange(NEW_USER_EMAIL);
        verify(userRepository, times(1)).save(any(UserDBO.class));
        verify(emailService, times(1)).sendEmailChangeEmail(any(User.class), anyString());
    }

    @Test
    void emailChange_whenValidToken_thenChangeEmail() {
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        userService.emailChange(DEFAULT_USER_ID, DEFAULT_USER_EMAIL_TOKEN, NEW_USER_EMAIL);
        verify(userRepository, times(1)).save(any(UserDBO.class));
    }

    @Test
    void emailChange_whenInvalidToken_thenChangeEmail() {
        var token = "invalidtoken";
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        assertThrows(InvalidEmailVerificationTokenException.class, () -> userService.emailChange(DEFAULT_USER_ID, token, NEW_USER_EMAIL));
    }

    @Test
    void passwordChange() {
        var userDBO = createDefaultUserDBO();
        var passwordChangeRequest = createDefaultPasswordChangeRequest();
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);

        userService.passwordChange(passwordChangeRequest);

        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);
        verify(userTransactions).tryToUpdateUser(captor.capture());
        userDBO = captor.getValue();

        assertEquals(NEW_USER_PASSWORD, userDBO.getMasterPasswordHash());
        assertEquals(NEW_USER_PASSWORD_HINT, userDBO.getMasterPasswordHint());
    }

    @Test
    void getAllUsers() {
        var userDBO = createDefaultUserDBO();
        when(userRepository.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(userDBO)));
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);

        Set<SimpleUserResponse> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void getCurrentUser() {
        var userDBO = createDefaultUserDBO();
        var userResponse = createDefaultUserResponse();
        when(requestHelperService.getUserName()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(mappingService.userDBOToResponse(userDBO)).thenReturn(userResponse);

        userResponse = userService.getCurrentUser();

        assertNotNull(userResponse);
    }
}