package app.yapam.user;

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
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
class UserControllerTest extends YapamBaseTest {

    @Autowired private MockMvc mvc;
    @MockBean private UserService userService;

    @Test
    void whenGetUserById_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_USERS_USER_BY_ID, DEFAULT_USER_ID)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenRegister_thenReturnSuccessful() throws Exception {
        mvc.perform(
                post(API_USERS_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{}")
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenEmailValidation_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_USERS_EMAIL_VERIFY_URL, DEFAULT_USER_ID)
                        .param(API_HEADER_TOKEN, DEFAULT_USER_EMAIL_TOKEN)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenEmailChangeRequest_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_USERS_CURRENT_USER_EMAIL_REQUEST_CHANGE)
                        .param(API_HEADER_EMAIL, NEW_USER_EMAIL)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenEmailChange_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_USERS_EMAIL_CHANGE, DEFAULT_USER_ID)
                        .param(API_HEADER_TOKEN, DEFAULT_USER_EMAIL_TOKEN)
                        .param(API_HEADER_EMAIL, NEW_USER_EMAIL)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenUpdatePassword_thenReturnSuccessful() throws Exception {
        mvc.perform(
                put(API_USERS_CURRENT_USER_PASSWORD_CHANGE)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void whenGetAllUsers_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_USERS_BASE_URL)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetCurrentUser_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_USERS_CURRENT_USER)
        )
                .andExpect(status().is2xxSuccessful());
    }
}