package app.yapam.common.service;

import app.yapam.secret.model.response.SimpleSecretResponse;
import org.springframework.stereotype.Service;

@Service
public class FilterEvaluator {

    public Boolean filterForKeywords(SimpleSecretResponse filterObject, String[] keywords) {
        if (keywords.length == 0) {
            return true;
        }
        for (String keyword : keywords) {
            if (filterObject.getTags().stream().anyMatch(t -> t.contains(keyword)) ||
                filterObject.getTitle().contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
