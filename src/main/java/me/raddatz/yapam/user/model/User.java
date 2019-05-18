package me.raddatz.yapam.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {

    private String id;
    private String name;
    private String email;
    @JsonIgnore
    private Boolean emailVerified;
    @JsonIgnore
    private String emailToken;
    @JsonIgnore
    private String masterPasswordHash;
    @JsonIgnore
    private String masterPasswordHint;
    private String publicKey;
    private String culture;
    @JsonIgnore
    private LocalDateTime creationDate;
}
