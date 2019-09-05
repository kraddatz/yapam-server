package app.yapam.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends YapamException {

    public InternalErrorException(String message) {
        super(message);
    }
}
