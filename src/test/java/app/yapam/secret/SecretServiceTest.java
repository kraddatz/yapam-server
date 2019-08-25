package app.yapam.secret;

import app.yapam.YapamBaseTest;
import app.yapam.common.repository.*;
import app.yapam.common.service.MappingService;
import app.yapam.common.service.RequestHelperService;
import app.yapam.secret.model.Secret;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecretService.class)
@ActiveProfiles("test")
class SecretServiceTest extends YapamBaseTest {

    private static SpelAwareProxyProjectionFactory projectionFactory;
    @Autowired private SecretService secretService;
    @MockBean private UserRepository userRepository;
    @MockBean private RequestHelperService requestHelperService;
    @MockBean private MappingService mappingService;
    @MockBean private SecretRepository secretRepository;
    @MockBean private UserSecretRepository userSecretRepository;

    @BeforeAll
    static void beforeAll() {
        projectionFactory = new SpelAwareProxyProjectionFactory();
    }

    @Test
    void createSecret() {
        var secretRequest = createDefaultSecretRequest();
        var secret = createDefaultSecret();
        var secretDao = createDefaultSecretDao();
        var secretDaoWithoutId = createDefaultSecretDao();
        var secretResponse = createDefaultSecretResponse();
        secretDaoWithoutId.setId(null);
        when(mappingService.secretFromRequest(secretRequest)).thenReturn(secret);
        when(mappingService.secretToDao(secret)).thenReturn(secretDaoWithoutId);
        when(secretRepository.save(any(SecretDao.class))).thenReturn(secretDao);
        when(mappingService.secretToResponse(any(Secret.class))).thenReturn(secretResponse);

        var result = secretService.createSecret(secretRequest);

        assertNotNull(result);
        verify(userSecretRepository, times(1)).saveAll(any());
    }

    @Test
    void deleteSecret() {
        secretService.deleteSecret(DEFAULT_SECRET_SECRETID);

        verify(secretRepository, times(1)).deleteBySecretId(DEFAULT_SECRET_SECRETID);
    }

    @Test
    void getAllSecrets() {
        var userDao = createDefaultUserDao();
        var userSecretDao = createDefaultUserSecretDao();
        var secretDao = createDefaultSecretDao();
        var simpleSecretResponse = createDefaultSimpleSecretResponse();
        when(requestHelperService.getEmail()).thenReturn(DEFAULT_USER_EMAIL);
        when(userRepository.findOneByEmail(DEFAULT_USER_EMAIL)).thenReturn(userDao);
        when(userSecretRepository.findAllByUserId(DEFAULT_USER_ID)).thenReturn(new HashSet<>(Collections.singletonList(userSecretDao)));
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDao);
        when(mappingService.secretDaoToSimpleResponse(secretDao)).thenReturn(simpleSecretResponse);

        var result = secretService.getAllSecrets();

        assertEquals(1, result.getSecrets().size());
    }

    @Test
    void getSecretById_whenVersionIs0_thenReturnLatestSecret() {
        var secretDBO = createDefaultSecretDao();
        var secretResponse = createDefaultSecretResponse();
        var secretVersion = 0;
        when(secretRepository.findFirstBySecretIdOrderByVersionDesc(DEFAULT_SECRET_SECRETID)).thenReturn(secretDBO);
        when(mappingService.secretDaoToResponse(secretDBO)).thenReturn(secretResponse);

        var result = secretService.getSecretById(DEFAULT_SECRET_SECRETID, secretVersion);

        assertNotNull(result);
    }

    @Test
    void getSecretById_whenVersionIsNot0_thenReturnSecretAtVersion() {
        var secretDBO = createDefaultSecretDao();
        var secretResponse = createDefaultSecretResponse();
        var secretVersion = Integer.valueOf(1);
        when(secretRepository.findFirstBySecretIdAndVersion(DEFAULT_SECRET_SECRETID, secretVersion)).thenReturn(secretDBO);
        when(mappingService.secretDaoToResponse(secretDBO)).thenReturn(secretResponse);

        var result = secretService.getSecretById(DEFAULT_SECRET_SECRETID, secretVersion);

        assertEquals(secretVersion, result.getVersion());
    }

    @Test
    void updateSecret() {
        var secretRequest = createDefaultSecretRequest();
        var secret = createDefaultSecret();
        var secretDBO = createDefaultSecretDao();
        var secretVersionProjection = projectionFactory.createProjection(SecretVersionProjection.class);
        secretVersionProjection.setVersion(0);
        secretDBO.setVersion(1);
        when(mappingService.secretFromRequest(secretRequest)).thenReturn(secret);
        when(secretRepository.findFirstDistinctVersionBySecretIdOrderByVersionDesc(anyString())).thenReturn(secretVersionProjection);
        when(mappingService.secretToDao(any(Secret.class))).thenReturn(secretDBO);

        secretService.updateSecret(DEFAULT_SECRET_SECRETID, secretRequest);

        ArgumentCaptor<SecretDao> captor = ArgumentCaptor.forClass(SecretDao.class);
        verify(secretRepository).save(captor.capture());
        secretDBO = captor.getValue();

        assertEquals(Integer.valueOf(1), secretDBO.getVersion());
    }
}
