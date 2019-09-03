package app.yapam.tag;

import app.yapam.YapamBaseTest;
import app.yapam.common.repository.SecretDao;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.repository.TagDao;
import app.yapam.common.repository.TagRepository;
import app.yapam.common.service.MappingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TagService.class)
@ActiveProfiles("test")
class TagServiceTest extends YapamBaseTest {

    @Autowired private TagService tagService;
    @MockBean private TagRepository tagRepository;
    @MockBean private SecretRepository secretRepository;
    @MockBean private MappingService mappingService;

    @Test
    void createTags() {
        var tagRequestWrapper = createDefaultTagRequestWrapper();
        var tagDao = createDefaultTagDao();
        var tagResponse = createDefaultTagResponse();
        when(tagRepository.save(any(TagDao.class))).thenReturn(tagDao);
        when(mappingService.tagDaoToResponse(tagDao)).thenReturn(tagResponse);

        var result = tagService.createTags(tagRequestWrapper);

        assertEquals(DEFAULT_TAG_ID, result.getTags().get(0).getId());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0).getName());
    }

    @Test
    void getTags() {
        var tagDao = createDefaultTagDao();
        var tagResponse = createDefaultTagResponse();
        when(tagRepository.findAll()).thenReturn(Collections.singletonList(tagDao));
        when(mappingService.tagDaoToResponse(tagDao)).thenReturn(tagResponse);

        var result = tagService.getTags();

        assertEquals(DEFAULT_TAG_ID, result.getTags().get(0).getId());
        assertEquals(DEFAULT_TAG_NAME, result.getTags().get(0).getName());
    }

    @Test
    void attachSecretToTags() {
        var tagDao = createDefaultTagDao();
        var tag = createDefaultTag();
        var secretDao = createDefaultSecretDao();
        when(tagRepository.findOneById(DEFAULT_TAG_ID)).thenReturn(tagDao);

        tagService.attachSecretToTags(Collections.singletonList(tag), secretDao);

        ArgumentCaptor<SecretDao> captor = ArgumentCaptor.forClass(SecretDao.class);
        verify(secretRepository, times(1)).save(captor.capture());

        secretDao = captor.getValue();
        assertEquals(DEFAULT_TAG_ID, secretDao.getTags().get(0).getId());
        assertEquals(DEFAULT_TAG_NAME, secretDao.getTags().get(0).getName());
    }
}
