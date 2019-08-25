package app.yapam.secret.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserIdSecretPrivilege {

    private String userId;
    private Boolean privileged;
}
