package app.yapam.tag;

import app.yapam.common.repository.SecretDao;
import app.yapam.common.repository.SecretRepository;
import app.yapam.common.repository.TagDao;
import app.yapam.common.repository.TagRepository;
import app.yapam.common.service.MappingService;
import app.yapam.tag.model.Tag;
import app.yapam.tag.model.request.TagRequestWrapper;
import app.yapam.tag.model.response.TagResponse;
import app.yapam.tag.model.response.TagResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired private TagRepository tagRepository;
    @Autowired private SecretRepository secretRepository;
    @Autowired private MappingService mappingService;

    public void attachSecretToTags(List<Tag> tagIds, SecretDao secretDao) {
        List<TagDao> tags = new ArrayList<>();
        for (Tag tag : tagIds) {
            var tagDao = tagRepository.findOneById(tag.getId());
            tags.add(tagDao);
        }
        secretDao.setTags(tags);
        secretRepository.save(secretDao);
    }

    TagResponseWrapper createTags(TagRequestWrapper tagRequestWrapper) {
        List<TagResponse> tags = new ArrayList<>();
        for (String tagName : tagRequestWrapper.getTags()) {
            var tagDao = new TagDao();
            tagDao.setName(tagName);
            tagDao = tagRepository.save(tagDao);
            tags.add(mappingService.tagDaoToResponse(tagDao));
        }
        var tagResponseWrapper = new TagResponseWrapper();
        tagResponseWrapper.setTags(tags);
        return tagResponseWrapper;
    }

    public TagResponseWrapper getTags() {
        var tags = tagRepository.findAll().stream().map(dao -> mappingService.tagDaoToResponse(dao)).collect(Collectors.toList());
        var tagResponseWrapper = new TagResponseWrapper();
        tagResponseWrapper.setTags(tags);
        return tagResponseWrapper;
    }
}
