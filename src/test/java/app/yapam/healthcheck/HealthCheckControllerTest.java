package app.yapam.healthcheck;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HealthCheckController.class)
@ActiveProfiles("test")
class HealthCheckControllerTest extends YapamBaseTest {

    @Autowired private MockMvc mvc;
    @MockBean private HealthCheckService healthCheckService;

    @Test
    void whenHealthCheckIsCalled_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_STATUS_URL).accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void whenInfoIsCalled_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_HEALTH_URL)
        )
                .andExpect(status().isOk());
    }

}