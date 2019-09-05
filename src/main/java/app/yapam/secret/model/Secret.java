package app.yapam.secret.model;

import app.yapam.file.model.File;
import app.yapam.tag.model.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Secret {

    private String title;
    private String secretId;
    private String data;
    private SecretTypeEnum type;
    private Integer version;
    private LocalDateTime creationDate;
    private List<UserSecretPrivilege> users;
    private List<File> files;
    private List<Tag> tags;
}
