package app.yapam.user;

import app.yapam.YapamBaseTest;
import app.yapam.common.error.UnknownUserException;
import app.yapam.common.repository.UserDao;
import app.yapam.common.repository.UserRepository;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.user.model.User;
import app.yapam.user.model.response.SimpleUserResponseWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@ActiveProfiles("test")
class UserServiceTest extends YapamBaseTest {

    @Autowired private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private MappingService mappingService;
    @MockBean private EmailService emailService;

    @Test
    void createUser() {
        var userRequest = createDefaultUserRequest();
        var user = createDefaultUser();
        var userResponse = createDefaultUserResponse();
        var userDao = createDefaultUserDao();
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);
        when(mappingService.userToDao(user)).thenReturn(userDao);
        when(userRepository.save(userDao)).thenReturn(userDao);
        when(mappingService.userDaoToResponse(userDao)).thenReturn(userResponse);

        var result = userService.createUser(userRequest);
        verify(emailService, times(1)).sendWelcomeMail(any(User.class));
        verify(userRepository, times(1)).save(any(UserDao.class));

        assertNotNull(result);
    }

    @Test
    void getAllUsers() {
        var userDBO = createDefaultUserDao();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userDBO));

        SimpleUserResponseWrapper users = userService.getAllUsers();

        assertEquals(1, users.getUsers().size());
    }

    @Test
    void getCurrentUser() {
        mockSecurityContextHolder();
        var userDBO = createDefaultUserDao();
        var userResponse = createDefaultUserResponse();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(mappingService.userDaoToResponse(userDBO)).thenReturn(userResponse);

        userResponse = userService.getCurrentUser();

        assertNotNull(userResponse);
    }

    @Test
    void getSimpleUserById() {
        var userDBO = createDefaultUserDao();
        var simpleUserResponse = createDefaultSimpleUserResponse();
        when(userRepository.findOneById(DEFAULT_USER_ID)).thenReturn(userDBO);
        when(mappingService.userDaoToSimpleResponse(userDBO)).thenReturn(simpleUserResponse);

        var result = userService.getSimpleUserById(DEFAULT_USER_ID);

        assertNotNull(result);
    }

    @Test
    void getSimpleUserById_whenUserNotFound_thenThrowException() {
        assertThrows(UnknownUserException.class, () ->userService.getSimpleUserById(DEFAULT_USER_ID));
    }
}
