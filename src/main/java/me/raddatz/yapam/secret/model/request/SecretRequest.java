package me.raddatz.yapam.secret.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.raddatz.yapam.secret.model.SecretType;

@Getter
@Setter
public class SecretRequest {

    @ApiModelProperty(value = "PGP encrypted data", example = "data")
    private String data;
    @ApiModelProperty(value = "Type of the secret", dataType = "string", allowableValues= "PASSWORD, ID, WIFI, NOTE")
    private SecretType type;
    @ApiModelProperty(value = "owner of the secret", example = "7ba292c2-7c9b-48ac-a0b5-edb223704f42")
    private String userId;
}
