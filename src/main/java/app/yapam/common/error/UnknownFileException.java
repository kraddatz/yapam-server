package app.yapam.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnknownFileException extends YapamException {

    public UnknownFileException() {
        super();
    }

    public UnknownFileException(String fileId) {
        super(String.format("File with id %s not found", fileId));
    }
}
