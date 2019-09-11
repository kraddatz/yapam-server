package app.yapam.auth;

import app.yapam.YapamBaseTest;
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
@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
class AuthControllerTest extends YapamBaseTest {

    @Autowired private MockMvc mvc;
    @MockBean private AuthService authService;

    @Test
    void whenGetFileById_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_AUTH_BASE_URL)
        )
                .andExpect(status().is2xxSuccessful());
    }

}
