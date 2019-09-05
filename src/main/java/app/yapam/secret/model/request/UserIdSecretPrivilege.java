package app.yapam.secret.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserIdSecretPrivilege {

    @ApiModelProperty(value = "Internal id of the user", example = "4df782d9-3618-417e-a6a6-54a85c23374d")
    private String userId;
    @ApiModelProperty(value = "User is privileged on this secret", example = "true")
    private Boolean privileged;
}
