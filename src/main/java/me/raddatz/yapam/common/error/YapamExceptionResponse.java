package me.raddatz.yapam.common.error;

import lombok.Getter;

@Getter
public class YapamExceptionResponse {

    private String error;

    public YapamExceptionResponse(YapamException ex) {
        this.error = ex.getError();
    }
}
