package me.raddatz.yapam.secret;

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
class SecretControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private SecretService secretService;

    @Test
    void whenCreateSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                post("/api//secrets")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenUpdateSecret_thenReturnSuccessful() throws Exception {
        mvc.perform(
                put("/api/secrets/{secretId}", "secretId")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetAllSecrets_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/secrets")
        )
                .andExpect(status().is2xxSuccessful());
    }
}
