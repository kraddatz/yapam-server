package app.yapam.secret;

import app.yapam.YapamBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecretController.class)
@ActiveProfiles("test")
class SecretControllerTest extends YapamBaseTest {

    @Autowired private MockMvc mvc;
    @MockBean private SecretService secretService;

    @Test
    void whenCreateSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                post(API_SECRETS_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenUpdateSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                put(API_SINGLE_SECRET_URL, "secretId")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetAllSecrets_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_SECRETS_BASE_URL)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetSecretById_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_SINGLE_SECRET_URL, "secretId")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenDeleteSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                delete(API_SINGLE_SECRET_URL, "secretId")
        )
                .andExpect(status().is2xxSuccessful());
    }
}
