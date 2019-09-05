package app.yapam.common.service;

import app.yapam.YapamBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FilterEvaluator.class)
@ActiveProfiles("test")
class FilterEvaluatorTest extends YapamBaseTest  {

    @Autowired private FilterEvaluator filterEvaluator;

    @Test
    void whenNoKeywords_thenReturnTrue() {
        var simpleSecretResponse = createDefaultSimpleSecretResponse();

        var result = filterEvaluator.filterForKeywords(simpleSecretResponse, new String[]{});

        assertTrue(result);
    }

    @Test
    void whenKeywordInTitle_thenReturnTrue() {
        var simpleSecretResponse = createDefaultSimpleSecretResponse();

        var result = filterEvaluator.filterForKeywords(simpleSecretResponse, new String[]{DEFAULT_SECRET_TITLE});

        assertTrue(result);
    }

    @Test
    void whenKeywordInTags_thenReturnTrue() {
        var simpleSecretResponse = createDefaultSimpleSecretResponse();

        var result = filterEvaluator.filterForKeywords(simpleSecretResponse, new String[]{DEFAULT_TAG_NAME});

        assertTrue(result);
    }

    @Test
    void whenKeywordNotInTagsAndTitle_thenReturnFalse() {
        var simpleSecretResponse = createDefaultSimpleSecretResponse();

        var result = filterEvaluator.filterForKeywords(simpleSecretResponse, new String[]{"NOT_FOUND_KEYWORD"});

        assertFalse(result);
    }
}
