package me.raddatz.yapam.secret.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.raddatz.yapam.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Secret {

    private String secretId;
    private String data;
    private SecretType type;
    private Integer version;
    private LocalDateTime creationDate;
    private User user;
}