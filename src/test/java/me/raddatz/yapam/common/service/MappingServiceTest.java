package me.raddatz.yapam.common.service;

import me.raddatz.yapam.secret.model.Secret;
import me.raddatz.yapam.secret.model.request.SecretRequest;
import me.raddatz.yapam.secret.repository.SecretDBO;
import me.raddatz.yapam.user.model.User;
import me.raddatz.yapam.user.model.request.UserRequest;
import me.raddatz.yapam.user.repository.UserDBO;
import me.raddatz.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MappingService.class)
@ActiveProfiles("test")
class MappingServiceTest {

    @Autowired private MappingService mappingService;
    @MockBean private UserRepository userRepository;

    private Secret createDefaultSecret() {
        var secret = new Secret();
        secret.setData("data");
        return secret;
    }

    private SecretDBO createDefaultSecretDBO() {
        var secretDBO = new SecretDBO();
        secretDBO.setData("Data");
        return secretDBO;
    }

    private SecretRequest createDefaultSecretRequest() {
        var secretRequest = new SecretRequest();
        secretRequest.setData("data");
        return secretRequest;
    }

    private UserDBO createDefaultUserDBO() {
        var userDBO = new UserDBO();
        userDBO.setName("username");
        return userDBO;
    }

    private User createDefaultUser() {
        var user = new User();
        user.setName("username");
        return user;
    }

    private UserRequest createDefaultUserRequest() {
        var userRequest = new UserRequest();
        userRequest.setName("username");
        return userRequest;
    }

    @Test
    void secretFromDBO_whenSecretDBOHasNoUser() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretFromDBO(secretDBO);
        assertEquals("Data", result.getData());
    }

    @Test
    void secretFromDBO_whenSecretDBOHasUser() {
        var secretDBO = createDefaultSecretDBO();
        secretDBO.setUser(createDefaultUserDBO());

        var result = mappingService.secretFromDBO(secretDBO);
        assertEquals("username", result.getUser().getName());
    }

    @Test
    void secretToDBO_whenSecretHasNoUser() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToDBO(secret);
        assertEquals("data", result.getData());
    }

    @Test
    void secretToDBO_whenSecretHasUser() {
        var secret = createDefaultSecret();
        secret.setUser(createDefaultUser());

        var result = mappingService.secretToDBO(secret);
        assertEquals("username", result.getUser().getName());
    }

    @Test
    void secretFromRequest_whenSecretRequestHasNoUserId() {
        var secretRequest = createDefaultSecretRequest();

        var result = mappingService.secretFromRequest(secretRequest);
        assertEquals("data", result.getData());
    }

    @Test
    void secretFromRequest_whenSecretRequestHasUserId() {
        var secretRequest = createDefaultSecretRequest();
        var userDBO = createDefaultUserDBO();
        secretRequest.setUserId("userId");
        when(userRepository.findOneById("userId")).thenReturn(userDBO);

        var result = mappingService.secretFromRequest(secretRequest);
        assertEquals("username", result.getUser().getName());
    }

    @Test
    void secretToResponse_whenSecretHasNoUser() {
        var secret = createDefaultSecret();

        var result = mappingService.secretToResponse(secret);
        assertEquals("data", result.getData());
    }

    @Test
    void secretToResponse_whenSecretHasUser() {
        var secret = createDefaultSecret();
        secret.setUser(createDefaultUser());

        var result = mappingService.secretToResponse(secret);
        assertEquals("username", result.getUser().getName());
    }

    @Test
    void secretDBOToResponse() {
        var secretDBO = createDefaultSecretDBO();

        var result = mappingService.secretDBOToResponse(secretDBO);
        assertTrue(true);
    }

    @Test
    void userFromDBO() {
        var userDBO = createDefaultUserDBO();

        var result = mappingService.userFromDBO(userDBO);
        assertEquals("username", result.getName());
    }

    @Test
    void userFromRequest() {
        var userRequest = createDefaultUserRequest();

        var result = mappingService.userFromRequest(userRequest);
        assertEquals("username", result.getName());
    }

    @Test
    void userToDBO() {
        var user = createDefaultUser();

        var result = mappingService.userToDBO(user);
        assertEquals("username", result.getName());
    }

    @Test
    void userToResponse() {
        var user = createDefaultUser();

        var result = mappingService.userToResponse(user);
        assertEquals("username", result.getName());
    }

    @Test
    void userDBOToSimpleResponse() {
        var userDBO = createDefaultUserDBO();

        var result = mappingService.userDBOToSimpleResponse(userDBO);
        assertEquals("username", result.getName());
    }
}