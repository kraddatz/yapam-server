package me.raddatz.jwarden.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class RegisterUser {
    @ApiModelProperty(value = "Name of the User", example = "Max Mustermann")
    private String name;
    @ApiModelProperty(value = "Email of the user", example = "max.mustermann@email.com")
    private String email;
    @ApiModelProperty(value = "Password of the user", example = "MasterPassword123")
    private String masterPassword;
    @ApiModelProperty(value = "Password hint", example = "Password is Password")
    private String masterPasswordHint;

    public User toUser() {
        var user = new User();
        user.setName(this.name);
        user.setEmail(this.email);
        user.setMasterPasswordHint(this.masterPasswordHint);
        return user;
    }

}
