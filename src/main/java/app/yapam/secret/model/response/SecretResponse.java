package app.yapam.secret.model.response;

import app.yapam.user.model.response.SimpleUserResponse;
import lombok.Getter;
import lombok.Setter;
import app.yapam.secret.model.SecretTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
public class SecretResponse {

    private String secretId;
    private String data;
    private SecretTypeEnum type;
    private Integer version;
    private LocalDateTime creationDate;
    private SimpleUserResponse user;

}
