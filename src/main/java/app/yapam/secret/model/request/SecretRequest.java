package app.yapam.secret.model.request;

import app.yapam.secret.model.SecretTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecretRequest {

    @ApiModelProperty(value = "Title of the secret", example = "yapam")
    private String title;
    @ApiModelProperty(value = "not yet PGP encrypted data", example = "\"{\"username\":\"username\",\"password\":\"password\",\"url\":\"url\",\"notes\":\"notes\"}\"")
    private String data;
    @ApiModelProperty(value = "Type of the secret", dataType = "string", allowableValues = "PASSWORD, ID, WIFI, NOTE")
    private SecretTypeEnum type;
    @ApiModelProperty(value = "owner of the secret", example = "7ba292c2-7c9b-48ac-a0b5-edb223704f42")
    private String userId;
}
