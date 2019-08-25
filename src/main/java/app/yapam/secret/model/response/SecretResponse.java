package app.yapam.secret.model.response;

import lombok.Getter;
import lombok.Setter;
import app.yapam.secret.model.SecretTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SecretResponse {

    private String title;
    private String secretId;
    private String data;
    private SecretTypeEnum type;
    private Integer version;
    private LocalDateTime creationDate;
    private List<SimpleUserPrivilegeResponse> users;

}
