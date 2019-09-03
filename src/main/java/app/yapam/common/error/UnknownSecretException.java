package app.yapam.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownSecretException extends YapamException {

    public UnknownSecretException() {
        super();
    }

    public UnknownSecretException(String secretId) {
        super(String.format("Secret with id %s not found", secretId));
    }
}
