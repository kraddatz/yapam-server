package app.yapam.file.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {

    private String filename;
    private String id;
    private Long filesize;
    private byte[] content;
    private String hash;
}
