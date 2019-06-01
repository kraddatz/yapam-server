package app.yapam.user;

import app.yapam.common.error.EmailAlreadyExistsException;
import app.yapam.common.error.EmailVerificationTokenExpiredException;
import app.yapam.common.error.InvalidEmailVerificationTokenException;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.user.model.request.PasswordChangeRequest;
import app.yapam.user.model.response.SimpleUserResponse;
import app.yapam.config.YapamProperties;
import app.yapam.user.model.User;
import app.yapam.user.model.request.UserRequest;
import app.yapam.user.model.response.UserResponse;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import app.yapam.user.repository.UserTransactions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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
class UserServiceTest {

    @Autowired private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private EmailService emailService;
    @MockBean private YapamProperties yapamProperties;
    @MockBean private UserTransactions userTransactions;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private MappingService mappingService;

    private UserResponse createDefaultUserResponse() {
        var user = new UserResponse();
        user.setName("Name");
        user.setEmail("user@email.com");
        return user;
    }

    private UserRequest createDefaultUserRequest() {
        var user = new UserRequest();
        user.setName("Name");
        user.setEmail("user@email.com");
        user.setMasterPasswordHash("$2a$10$dmhu8DuXzuILmtCZ/QM.AOlBnLsb.Lo06reyMeyRmGDxXGNSV.nfK");
        return user;
    }

    private PasswordChangeRequest createDefaultPasswordChangeRequest() {
        var passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setMasterPasswordHash("$2a$10$9lAsYfGz8LVxNEr4NB8CHuMjd2H1rlq7YG8eSqpVYYNopZmdsW0ZO");
        passwordChangeRequest.setMasterPasswordHint("password is new_password");
        return passwordChangeRequest;
    }

    private User createDefaultUser() {
        var user = new User();
        user.setName("Name");
        user.setEmailToken("token");
        user.setCreationDate(LocalDateTime.now());
        user.setEmailVerified(false);
        user.setEmail("user@email.com");
        user.setMasterPasswordHash("$2a$10$dmhu8DuXzuILmtCZ/QM.AOlBnLsb.Lo06reyMeyRmGDxXGNSV.nfK");
        return user;
    }

    private UserDBO createDefaultUserDBO() {
        var userDBO = new UserDBO();
        userDBO.setName("Name");
        userDBO.setEmailToken("token");
        userDBO.setCreationDate(LocalDateTime.now());
        userDBO.setEmailVerified(false);
        userDBO.setEmail("user@email.com");
        userDBO.setMasterPasswordHash("$2a$10$dmhu8DuXzuILmtCZ/QM.AOlBnLsb.Lo06reyMeyRmGDxXGNSV.nfK");
        userDBO.setMasterPasswordHint("password is password");
        return userDBO;
    }

    @Test
    void register_whenUserNotExists_thenRegisterUser() {
        var registerUser = createDefaultUserRequest();
        var user = createDefaultUser();
        var userDBO = createDefaultUserDBO();
        when(mappingService.userFromRequest(Mockito.any(UserRequest.class))).thenReturn(user);
        when(mappingService.userToDBO(user)).thenReturn(userDBO);

        userService.createUser(registerUser);

        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);
        verify(userTransactions).tryToCreateUser(captor.capture());
        userDBO = captor.getValue();

        assertEquals(registerUser.getEmail(), userDBO.getEmail());
        assertEquals(registerUser.getName(), userDBO.getName());
        assertEquals("$2a$10$dmhu8DuXzuILmtCZ/QM.AOlBnLsb.Lo06reyMeyRmGDxXGNSV.nfK", userDBO.getMasterPasswordHash());
    }

    @Test
    void register_whenUserExistsInRegistrationUnverified_thenOverrideUser() {
        var registerUser = createDefaultUserRequest();
        var user = createDefaultUser();
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneByEmail(Mockito.anyString())).thenReturn(userDBO);
        when(mappingService.userFromRequest(Mockito.any(UserRequest.class))).thenReturn(user);
        when(mappingService.userToDBO(user)).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));

        userService.createUser(registerUser);

        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);
        verify(userTransactions).tryToCreateUser(captor.capture());
        userDBO = captor.getValue();

        assertEquals(registerUser.getEmail(), userDBO.getEmail());
        assertEquals(registerUser.getName(), userDBO.getName());
        assertEquals("$2a$10$dmhu8DuXzuILmtCZ/QM.AOlBnLsb.Lo06reyMeyRmGDxXGNSV.nfK", userDBO.getMasterPasswordHash());
    }

    @Test
    void register_whenUserExistsNotInRegistrationUnverified_thenThrowException() {
        var userRequest = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        userDBO.setCreationDate(LocalDateTime.now().minusDays(2));
        when(userRepository.findOneByEmail(Mockito.anyString())).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void register_whenUserExistsNotInRegistrationVerified_thenThrowException() {
        var userRequest = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        userDBO.setEmailVerified(true);
        userDBO.setCreationDate(LocalDateTime.now().minusDays(2));
        when(userRepository.findOneByEmail(Mockito.anyString())).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void register_whenUserExistsInRegistrationVerified_thenThrowException() {
        var userRequest = createDefaultUserRequest();
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        userDBO.setEmailVerified(true);
        when(userRepository.findOneByEmail(Mockito.anyString())).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void verifyEmail_validToken_thenSaveUser() {
        var userId = "userid";
        var token = "token";
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(anyString())).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));

        userService.verifyEmail(userId, token);
        verify(userRepository, times(1)).save(any(UserDBO.class));
    }

    @Test
    void verifyEmail_whenInvalidToken_thenThrowException() {
        var userId = "userid";
        var token = "invalidtoken";
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(anyString())).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));

        assertThrows(InvalidEmailVerificationTokenException.class, () -> userService.verifyEmail(userId, token));
    }

    @Test
    void verifyEmail_whenUserNotInRegistrationPeriod_thenThrowException() {
        var userId = "userid";
        var token = "invalidtoken";
        var userDBO = createDefaultUserDBO();
        userDBO.setCreationDate(LocalDateTime.now().minusDays(2));
        when(userRepository.findOneById(anyString())).thenReturn(userDBO);
        when(yapamProperties.getRegistrationTimeout()).thenReturn(Duration.parse("P1D"));

        assertThrows(EmailVerificationTokenExpiredException.class, () -> userService.verifyEmail(userId, token));
    }

    @Test
    void requestEmailChange_whenUserExists_thenRequestEmailChange() {
        var userId = "userId";
        var email = "email";
        var userDBO = createDefaultUserDBO();
        var user = createDefaultUser();
        when(userRepository.findOneById(anyString())).thenReturn(userDBO);
        when(mappingService.userFromDBO(userDBO)).thenReturn(user);

        userService.requestEmailChange(userId, email);
        verify(userRepository, times(1)).save(any(UserDBO.class));
        verify(emailService, times(1)).sendEmailChangeEmail(any(User.class), anyString());
    }

    @Test
    void emailChange_whenValidToken_thenChangeEmail() {
        var userId = "userId";
        var token = "token";
        var email = "email";
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(anyString())).thenReturn(userDBO);

        userService.emailChange(userId, token, email);
        verify(userRepository, times(1)).save(any(UserDBO.class));
    }

    @Test
    void emailChange_whenInvalidToken_thenChangeEmail() {
        var userId = "userId";
        var token = "invalidtoken";
        var email = "email";
        var userDBO = createDefaultUserDBO();
        when(userRepository.findOneById(anyString())).thenReturn(userDBO);

        assertThrows(InvalidEmailVerificationTokenException.class, () -> userService.emailChange(userId, token, email));
    }

    @Test
    void passwordChange() {
        var userDBO = createDefaultUserDBO();
        var passwordChangeRequest = createDefaultPasswordChangeRequest();
        when(requestHelperService.getUserName()).thenReturn("user@email.com");
        when(userRepository.findOneByEmail(anyString())).thenReturn(userDBO);

        userService.passwordChange(passwordChangeRequest);

        ArgumentCaptor<UserDBO> captor = ArgumentCaptor.forClass(UserDBO.class);
        verify(userTransactions).tryToUpdateUser(captor.capture());
        userDBO = captor.getValue();

        assertEquals("$2a$10$9lAsYfGz8LVxNEr4NB8CHuMjd2H1rlq7YG8eSqpVYYNopZmdsW0ZO", userDBO.getMasterPasswordHash());
        assertEquals("password is new_password", userDBO.getMasterPasswordHint());
    }

    @Test
    void getAllUsers() {
        var userDBO = createDefaultUserDBO();
        when(userRepository.findAll()).thenReturn(new ArrayList<>(Collections.singletonList(userDBO)));

        Set<SimpleUserResponse> users = userService.getAllUsers();

        assertEquals(1, users.size());
    }

    @Test
    void getCurrentUser() {
        var userDBO = createDefaultUserDBO();
        var userResponse = createDefaultUserResponse();
        when(requestHelperService.getUserName()).thenReturn("user@email.com");
        when(userRepository.findOneByEmail("user@email.com")).thenReturn(userDBO);
        when(mappingService.userDBOToResponse(userDBO)).thenReturn(userResponse);

        userResponse = userService.getCurrentUser();

        assertNotNull(userResponse);
    }
}