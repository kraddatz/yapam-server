package me.raddatz.yapam.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.raddatz.yapam.secret.model.Secret;
import me.raddatz.yapam.user.repository.UserDBO;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private Boolean emailVerified;
    private String emailToken;
    private String masterPasswordHash;
    private String masterPasswordHint;
    private String masterPasswordSalt;
    private String culture;
    private LocalDateTime creationDate;
    private Set<Secret> secrets;

    public User (RegisterUser registerUser) {
        BeanUtils.copyProperties(registerUser, this);
    }

    public User (UserDBO userDBO) {
        BeanUtils.copyProperties(userDBO, this);
    }

    public UserDBO toDBO() {
        var userDBO = new UserDBO();
        BeanUtils.copyProperties(this, userDBO);
        return userDBO;
    }
}
