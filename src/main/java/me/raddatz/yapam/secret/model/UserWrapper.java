package me.raddatz.yapam.secret.model;

import lombok.Getter;
import lombok.Setter;
import me.raddatz.yapam.user.model.User;

@Getter
@Setter
public class UserWrapper {

    private User user;
    private Boolean privileged;
}
