package app.yapam.tag;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagController.class)
@ActiveProfiles("test")
public class TagControllerTest extends YapamBaseTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TagService tagService;

    @Test
    void createTags() throws Exception {
        mockMvc.perform(
                post(API_TAGS_BASE_URL)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getTags() throws Exception {
        mockMvc.perform(
                get(API_TAGS_BASE_URL)
        )
                .andExpect(status().is2xxSuccessful());
    }
}
