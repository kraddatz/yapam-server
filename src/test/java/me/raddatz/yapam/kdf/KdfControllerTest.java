package me.raddatz.yapam.kdf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(KdfController.class)
@ActiveProfiles("test")
class KdfControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private KdfService kdfService;

    @Test
    void whenGetKdfInfo_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/kdf")
        )
                .andExpect(status().is2xxSuccessful());
    }

}