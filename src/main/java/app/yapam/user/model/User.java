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
    private Boolean emailVerified;
    private String emailToken;
    private String masterPasswordHash;
    private String masterPasswordHint;
    private String publicKey;
    private String culture;
    private LocalDateTime creationDate;
}
