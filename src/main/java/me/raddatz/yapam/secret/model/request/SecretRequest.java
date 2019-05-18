package me.raddatz.yapam.secret.model.request;

import lombok.Getter;
import lombok.Setter;
import me.raddatz.yapam.secret.model.SecretType;

@Getter
@Setter
public class SecretRequest {
    private String data;
    private SecretType type;
    private String userId;
}
