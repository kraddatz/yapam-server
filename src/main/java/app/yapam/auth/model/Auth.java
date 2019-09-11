package app.yapam.auth.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {

    private String identityProviderUrl;
    private String clientId;
}
