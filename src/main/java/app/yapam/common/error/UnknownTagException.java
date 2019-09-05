package app.yapam.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownTagException extends YapamException {

    public UnknownTagException() {
        super();
    }

    public UnknownTagException(String tagId) {
        super(String.format("Tag with id %s not found", tagId));
    }
}
