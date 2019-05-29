package app.yapam.secret.model.request;

import app.yapam.secret.model.SecretTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecretRequest {

    @ApiModelProperty(value = "PGP encrypted data", example = "data")
    private String data;
    @ApiModelProperty(value = "Type of the secret", dataType = "string", allowableValues= "PASSWORD, ID, WIFI, NOTE")
    private SecretTypeEnum type;
    @ApiModelProperty(value = "owner of the secret", example = "7ba292c2-7c9b-48ac-a0b5-edb223704f42")
    private String userId;
}
