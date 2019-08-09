package app.yapam.user;

import app.yapam.YapamBaseTest;
import app.yapam.common.service.EmailService;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.user.model.User;
import app.yapam.user.model.response.SimpleUserResponseWrapper;
import app.yapam.user.repository.UserDao;
import app.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
@ActiveProfiles("test")
class UserServiceTest extends YapamBaseTest {

    @Autowired private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private MappingService mappingService;
    @MockBean private EmailService emailService;

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
    void createUser() {
        var userRequest = createDefaultUserRequest();
        var user = createDefaultUser();
        var userResponse = createDefaultUserResponse();
        var userDao = createDefaultUserDao();
        when(mappingService.userFromRequest(userRequest)).thenReturn(user);
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(mappingService.userToResponse(user)).thenReturn(userResponse);
        when(mappingService.userToDao(user)).thenReturn(userDao);

        var result = userService.createUser(userRequest);
        verify(emailService, times(1)).sendWelcomeMail(any(User.class));
        verify(userRepository, times(1)).save(any(UserDao.class));

        assertNotNull(result);
    }

    @Test
    void getAllUsers() {
        var userDBO = createDefaultUserDao();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(userDBO));
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);

        SimpleUserResponseWrapper users = userService.getAllUsers();

        assertEquals(1, users.getUsers().size());
    }

    @Test
    void getCurrentUser() {
        var userDBO = createDefaultUserDao();
        var userResponse = createDefaultUserResponse();
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDBO);
        when(mappingService.userDaoToResponse(userDBO)).thenReturn(userResponse);

        userResponse = userService.getCurrentUser();

        assertNotNull(userResponse);
    }
}
