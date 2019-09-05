package app.yapam.tag.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagResponseWrapper {

    private List<TagResponse> tags;
}
