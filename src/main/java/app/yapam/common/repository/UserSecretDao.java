package app.yapam.common.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "user_secret")
public class UserSecretDao {

    @EmbeddedId
    private UserSecretId id = new UserSecretId();

    @ManyToOne
    @MapsId("secretId")
    private SecretDao secret;

    @ManyToOne
    @MapsId("userId")
    private UserDao user;

    private Boolean privileged;

    public UserSecretDao(SecretDao secretDao, UserDao userDao, Boolean privileged) {
        this.secret = secretDao;
        this.user = userDao;
        this.privileged = privileged;
    }
}
