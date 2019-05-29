package app.yapam.user.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleUserResponse {

    private String id;
    private String name;
    private String email;
    private String publicKey;
}
