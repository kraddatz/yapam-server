package app.yapam.file.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileResponse {

    private String filename;
    private Long filesize;
    private byte[] content;
    private String id;
}
