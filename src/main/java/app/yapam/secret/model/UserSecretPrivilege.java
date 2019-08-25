package app.yapam.secret.model;

import app.yapam.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSecretPrivilege {

    private User user;
    private Boolean privilege;
}
