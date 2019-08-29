package app.yapam.file.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleFileResponse {

    private String filename;
    private Long filesize;
    private String id;
}
