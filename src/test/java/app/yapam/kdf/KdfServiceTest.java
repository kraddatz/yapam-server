package app.yapam.kdf;

import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.config.YapamProperties;
import app.yapam.user.model.User;
import app.yapam.user.repository.UserDBO;
import app.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(KdfService.class)
@ActiveProfiles("test")
class KdfServiceTest {

    @Autowired private KdfService kdfService;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private YapamProperties yapamProperties;
    @MockBean private MappingService mappingService;
    @MockBean private UserRepository userRepository;

    private YapamProperties.Security createDefaultYapamSecurityProperties() {
        var security = new YapamProperties.Security();
        security.setBcryptIterations(10);
        return security;
    }

    private UserDBO createDefaultUserDBO() {
        var userDBO = new UserDBO();
        userDBO.setMasterPasswordHash("$2a$10$2t0Y7BXreyVzBrvPiyqcDOR/dwUyTMpxAhVJBVEy0YUBUXbNWC/Tq");
        return userDBO;
    }

    private User createDefaultUser() {
        var user = new User();
        user.setMasterPasswordHash("$2a$10$2t0Y7BXreyVzBrvPiyqcDOR/dwUyTMpxAhVJBVEy0YUBUXbNWC/Tq");
        return user;
    }

    @Test
    void whenKdfIterationsOutdated() {
        var yapamSecurityProperties = createDefaultYapamSecurityProperties();
        yapamSecurityProperties.setBcryptIterations(11);
        when(yapamProperties.getSecurity()).thenReturn(yapamSecurityProperties);
        when(requestHelperService.getUserName()).thenReturn("user@email.com");
        when(userRepository.findOneByEmail("user@email.com")).thenReturn(createDefaultUserDBO());
        when(mappingService.userFromDBO(any(UserDBO.class))).thenReturn(createDefaultUser());

        var result = kdfService.getKdfInfo();
        assertFalse(result.getSecure());
        assertEquals(Integer.valueOf(11), result.getIterations());
    }

    @Test
    void whenKdfIterationsNotOutdated() {
        var yapamSecurityProperties = createDefaultYapamSecurityProperties();
        when(yapamProperties.getSecurity()).thenReturn(yapamSecurityProperties);
        when(requestHelperService.getUserName()).thenReturn("user@email.com");
        when(userRepository.findOneByEmail("user@email.com")).thenReturn(createDefaultUserDBO());
        when(mappingService.userFromDBO(any(UserDBO.class))).thenReturn(createDefaultUser());

        var result = kdfService.getKdfInfo();
        assertTrue(result.getSecure());
        assertEquals(Integer.valueOf(10), result.getIterations());
    }
}