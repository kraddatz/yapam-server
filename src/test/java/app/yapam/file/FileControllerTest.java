package app.yapam.file;

import app.yapam.YapamBaseTest;
import app.yapam.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileController.class)
@ActiveProfiles("test")
class FileControllerTest extends YapamBaseTest {

    @Autowired private MockMvc mvc;
    @MockBean private FileService fileService;

    @Test
    void whenGetFileById_thenReturnSuccessful() throws Exception {
        mvc.perform(
                get(API_FILES_FILE_BY_ID, DEFAULT_FILE_ID)
        )
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenPostFile_thenReturnSuccessful() throws Exception {
        mvc.perform(
                multipart(API_FILES_BASE_URL)
                .file(createDefaultMultipartFile())
        )
                .andExpect(status().is2xxSuccessful());
    }
}
