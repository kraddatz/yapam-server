package app.yapam.user.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {

    private String id;
    private String name;
    private String email;
    private String publicKey;
    private String locale;
    private LocalDateTime creationDate;
}
