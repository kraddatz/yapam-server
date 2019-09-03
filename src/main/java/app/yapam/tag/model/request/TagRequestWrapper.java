package app.yapam.tag.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagRequestWrapper {

    private List<String> tags;
}
