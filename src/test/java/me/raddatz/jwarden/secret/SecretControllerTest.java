package me.raddatz.jwarden.secret;

import me.raddatz.jwarden.common.annotation.AnnotationHandlerInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecretController.class)
@ActiveProfiles("test")
class SecretControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private SecretService secretService;

    @Test
    @DisplayName("Test createSecret")
    void whenCreateSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                post("/secrets")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test updateSecret")
    void whenUpdateSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                post("/secrets/{secretId}", "secretId")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }
}
