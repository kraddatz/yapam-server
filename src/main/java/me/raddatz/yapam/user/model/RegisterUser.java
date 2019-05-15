package me.raddatz.yapam.user.model;

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
    @ApiModelProperty(value = "Hash of the password of the user", example = "$2a$10$dmhu8DuXzuILmtCZ/QM.AOlBnLsb.Lo06reyMeyRmGDxXGNSV.nfK")
    private String masterPasswordHash;
    @ApiModelProperty(value = "Password hint", example = "Password is Password")
    private String masterPasswordHint;
}
