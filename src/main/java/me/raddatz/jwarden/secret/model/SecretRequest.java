package me.raddatz.jwarden.secret.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecretRequest {
    private String data;
    private SecretType type;
}
