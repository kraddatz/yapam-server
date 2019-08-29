package app.yapam.common.error;

import lombok.Getter;

@Getter
public class YapamExceptionResponse {

    private String error;
    private String message;

    public YapamExceptionResponse(YapamException ex) {
        this.error = ex.getError();
        this.message = ex.getMessage();
    }
}
