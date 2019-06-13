package app.yapam.user.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {

    @ApiModelProperty(value = "BCrypt hash of the password of the user", example = "$2a$10$2t0Y7BXreyVzBrvPiyqcDOR/dwUyTMpxAhVJBVEy0YUBUXbNWC/Tq")
    private String masterPasswordHash;
    @ApiModelProperty(value = "Password hint", example = "Password is Password")
    private String masterPasswordHint;
}
