package me.raddatz.yapam.user.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class UserRequest {

    @ApiModelProperty(value = "Name of the User", example = "Max Mustermann")
    private String name;
    @ApiModelProperty(value = "Email of the userSecrets", example = "max.mustermann@email.com")
    private String email;
    @ApiModelProperty(value = "Hash of the password of the userSecrets", example = "$2a$10$2t0Y7BXreyVzBrvPiyqcDOR/dwUyTMpxAhVJBVEy0YUBUXbNWC/Tq")
    private String masterPasswordHash;
    @ApiModelProperty(value = "Password hint", example = "Password is Password")
    private String masterPasswordHint;
    @ApiModelProperty(value = "Public key of the userSecrets", example = "")
    private String publicKey;
}
