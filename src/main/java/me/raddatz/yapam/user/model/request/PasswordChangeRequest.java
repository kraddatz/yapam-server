package me.raddatz.yapam.user.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {

    @ApiModelProperty(value = "Hash of the password of the userSecrets", example = "$2a$10$2t0Y7BXreyVzBrvPiyqcDOR/dwUyTMpxAhVJBVEy0YUBUXbNWC/Tq")
    private String masterPasswordHash;
    @ApiModelProperty(value = "Password hint", example = "Password is Password")
    private String masterPasswordHint;
}
