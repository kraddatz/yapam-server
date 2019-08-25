package app.yapam.secret.model.request;

import app.yapam.secret.model.SecretTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecretRequest {

    @ApiModelProperty(value = "Title of the secret", example = "yapam")
    private String title;
    @ApiModelProperty(value = "not yet PGP encrypted data", example = "\"{\"username\":\"username\",\"password\":\"password\",\"url\":\"url\",\"notes\":\"notes\"}\"")
    private String data;
    @ApiModelProperty(value = "Type of the secret", dataType = "string", allowableValues = "LOGIN, ID, WIFI, NOTE, CREDITCARD")
    private SecretTypeEnum type;
    private List<UserIdSecretPrivilege> users;
}
