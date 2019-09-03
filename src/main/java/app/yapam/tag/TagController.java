package app.yapam.tag;

import app.yapam.tag.model.request.TagRequestWrapper;
import app.yapam.tag.model.response.TagResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {

    @Autowired private TagService tagService;

    @PostMapping(value = "/api/tags")
    public TagResponseWrapper createTags(@RequestBody TagRequestWrapper tagRequestWrapper) {
        return tagService.createTags(tagRequestWrapper);
    }

    @GetMapping(value = "/api/tags")
    public TagResponseWrapper getTags() {
        return tagService.getTags();
    }
}
