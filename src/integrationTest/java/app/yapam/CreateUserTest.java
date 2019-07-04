package app.yapam;

import app.yapam.test.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class CreateUserTest {

    @Autowired private DatabaseCleanup databaseCleanup;
    @Autowired private MockMvc mockMvc;
    @MockBean private JavaMailSender javaMailSender;

    @BeforeEach
    void before() {
        databaseCleanup.execute();
    }

    @Test
    void createValidUser() throws Exception {
        mockMvc.perform(
                post("/api/users")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("" +
                        "{" +
                        "  \"name\": \"Max Mustermann\",\n" +
                        "  \"email\": \"max.mustermann@email.com\",\n" +
                        "  \"culture\": \"de-DE\",\n" +
                        "  \"masterPasswordHint\": \"passwordispassword\",\n" +
                        "  \"masterPasswordHash\": \"$2a$10$zN1g7VOOpnBqI9CA/4vlTOgH8lfpzKlLgvdeLjHW/Advcz54Faiyq\",\n" +
                        "  \"publicKey\": \"publicKey\"\n" +
                        "}")
        ).andExpect(
                jsonPath("$.id").exists()
        ).andExpect(
                jsonPath("$.name").value("Max Mustermann")
        ).andExpect(
                jsonPath("$.email").value("max.mustermann@email.com")
        ).andExpect(
                jsonPath("$.emailVerified").value(false)
        ).andExpect(
                jsonPath("$.culture").value("de-DE")
        ).andExpect(
                jsonPath("$.creationDate").exists()
        ).andExpect(
                jsonPath("$.publicKey").value("publicKey")
        );
    }
}
