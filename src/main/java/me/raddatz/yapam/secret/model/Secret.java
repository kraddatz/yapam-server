package me.raddatz.yapam.secret.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.raddatz.yapam.secret.repository.SecretDBO;
import me.raddatz.yapam.user.model.User;
import org.springframework.beans.BeanUtils;

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

    public Secret (SecretRequest secretRequest) {
        BeanUtils.copyProperties(secretRequest, this);
    }

    public SecretDBO toDBO() {
        var secretDBO = new SecretDBO();
        BeanUtils.copyProperties(this, secretDBO);
        secretDBO.setUser(this.user.toDBO());
        return secretDBO;
    }
}
