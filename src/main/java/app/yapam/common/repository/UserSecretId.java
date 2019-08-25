package app.yapam.common.repository;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserSecretId implements Serializable {

    private String userId;
    private String secretId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSecretId that = (UserSecretId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(secretId, that.secretId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, secretId);
    }
}
