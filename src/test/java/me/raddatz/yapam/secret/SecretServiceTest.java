package me.raddatz.yapam.secret;

import me.raddatz.yapam.common.error.UserNotFoundException;
import me.raddatz.yapam.common.service.RequestHelperService;
import me.raddatz.yapam.secret.model.Secret;
import me.raddatz.yapam.secret.model.SecretRequest;
import me.raddatz.yapam.secret.repository.SecretRepository;
import me.raddatz.yapam.secret.repository.SecretTransaction;
import me.raddatz.yapam.user.repository.UserDBO;
import me.raddatz.yapam.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecretService.class)
@ActiveProfiles("test")
public class SecretServiceTest {

    @Autowired private SecretService secretService;
    @MockBean private SecretRepository secretRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private SecretTransaction secretTransaction;

    private UserDBO createDefaultUserDBO() {
        return new UserDBO();
    }

    private SecretRequest createDefaultSecret() {
        return new SecretRequest();
    }

    @Test
    void createSecret_whenUserForSecretExists_thenCreateSecret() {
        var user = createDefaultUserDBO();
        var secretRequest = createDefaultSecret();
        when(requestHelperService.getUserName()).thenReturn("user@email.com");
        when(userRepository.findOneByEmail("user@email.com")).thenReturn(user);

        secretService.createSecret(secretRequest);

        verify(secretTransaction, times(1)).saveSecret(any(Secret.class));
    }

    @Test
    void createSecret_whenUserForSecretNotExists_thenCreateSecret() {
        var secret = createDefaultSecret();
        when(requestHelperService.getUserName()).thenReturn("unknownuser@email.com");
        when(userRepository.findOneByEmail("unknownuser@email.com")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> secretService.createSecret(secret));
    }

    @Test
    void updateSecret_whenUserForSecretNotExists_thenCreateSecret() {
        var secret = createDefaultSecret();
        var secretId = "a04bc4a9-0dd7-4452-a804-8310cb4bac5c";
        when(requestHelperService.getUserName()).thenReturn("unknownuser@email.com");
        when(userRepository.findOneByEmail("unknownuser@email.com")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> secretService.updateSecret(secretId, secret));
    }
    @Test
    void updateSecret_whenUserForSecretExists_thenCreateSecret() {
        var secret = createDefaultSecret();
        var secretId = "a04bc4a9-0dd7-4452-a804-8310cb4bac5c";
        var user = createDefaultUserDBO();
        when(requestHelperService.getUserName()).thenReturn("user@email.com");
        when(userRepository.findOneByEmail("user@email.com")).thenReturn(user);
        when(secretRepository.findFirstVersionBySecretIdOrderByVersionDesc(secretId)).thenReturn(0);

        secretService.updateSecret(secretId, secret);

        ArgumentCaptor<Secret> captor = ArgumentCaptor.forClass(Secret.class);
        verify(secretTransaction).saveSecret(captor.capture());
        var secretDBO = captor.getValue();

        assertEquals(Integer.valueOf(1), secretDBO.getVersion());
    }

}
