package app.yapam.secret.model.response;

import app.yapam.user.model.response.SimpleUserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SimpleUserPrivilegeResponse {

    private SimpleUserResponse user;
    private Boolean privileged;
}

