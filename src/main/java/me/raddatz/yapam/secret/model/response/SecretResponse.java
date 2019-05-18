package me.raddatz.yapam.secret.model.response;

import lombok.Getter;
import lombok.Setter;
import me.raddatz.yapam.secret.model.SecretType;
import me.raddatz.yapam.user.model.response.SimpleUserResponse;

import java.time.LocalDateTime;

@Getter
@Setter
public class SecretResponse {

    private String secretId;
    private String data;
    private SecretType type;
    private Integer version;
    private LocalDateTime creationDate;
    private SimpleUserResponse user;

}
