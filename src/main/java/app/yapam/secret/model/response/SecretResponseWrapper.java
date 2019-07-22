package app.yapam.secret.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SecretResponseWrapper {

    private Set<SecretResponse> secrets;
}
