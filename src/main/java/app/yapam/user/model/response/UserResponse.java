package app.yapam.user.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {

    private String id;
    private String name;
    private String email;
    private String locale;
    private LocalDateTime creationDate;
    private String publicKey;
}
