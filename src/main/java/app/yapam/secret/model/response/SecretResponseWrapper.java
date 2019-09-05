package app.yapam.secret.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecretResponseWrapper {

    private List<SimpleSecretResponse> secrets;
}
