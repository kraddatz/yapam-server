package app.yapam.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class UserRequest {

    @ApiModelProperty(value = "Name of the user", example = "Max Mustermann")
    private String name;
    @ApiModelProperty(value = "Culture of the user", example = "de-DE")
    private String locale;
    @ApiModelProperty(value = "Public key of the user", example = "public_key")
    private String publicKey;
    @ApiModelProperty(value = "Email of the user", example = "max.mustermann@email.com")
    private String email;
}
