package app.yapam.user;

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
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private UserService userService;

    @Test
    void whenRegister_thenReturnSuccessful() throws Exception {
        mvc.perform(
                post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenEmailValidation_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/currentuser/email/verify")
                        .param("token", "token")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenEmailChangeRequest_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/currentuser/email/requestChange")
                        .param("email", "new@email.com")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenEmailChange_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/currentuser/email/change")
                        .param("token", "token")
                        .param("email", "new@email.com")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenUpdatePassword_thenReturnSuccessful() throws Exception {
        mvc.perform(
                put("/api/currentuser/password/change")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void whenGetAllUsers_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/users")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetCurrentUser_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get("/api/currentuser")
        )
                .andExpect(status().is2xxSuccessful());
    }
}