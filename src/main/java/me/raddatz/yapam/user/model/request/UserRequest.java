package me.raddatz.yapam.user.model.request;

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
    @ApiModelProperty(value = "Email of the user", example = "max.mustermann@email.com")
    private String email;
    @ApiModelProperty(value = "Hash of the password of the user", example = "$2a$10$2t0Y7BXreyVzBrvPiyqcDOR/dwUyTMpxAhVJBVEy0YUBUXbNWC/Tq")
    private String masterPasswordHash;
    @ApiModelProperty(value = "Password hint", example = "password is password")
    private String masterPasswordHint;
    @ApiModelProperty(value = "Public key of the userSecrets", example = "public_key")
    private String publicKey;
}
