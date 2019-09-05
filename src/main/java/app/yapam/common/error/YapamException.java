package app.yapam.common.error;

import lombok.Getter;

@Getter
public class YapamException extends RuntimeException {

    private final String error;
    private final String message;

    public YapamException() {
        super();
        this.error = this.getClass().getSimpleName();
        this.message = null;
    }

    public YapamException(String message) {
        super();
        this.error = this.getClass().getSimpleName();
        this.message = message;
    }

}
