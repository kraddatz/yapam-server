package app.yapam.secret.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import app.yapam.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Secret {

    private String secretId;
    private String data;
    private SecretTypeEnum type;
    private Integer version;
    private LocalDateTime creationDate;
    private User user;
}
