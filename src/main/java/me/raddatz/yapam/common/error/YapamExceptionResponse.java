package me.raddatz.yapam.common.error;

import lombok.Getter;

@Getter
public class YapamExceptionResponse {

    private String description;

    public YapamExceptionResponse(YapamException ex) {
        this.description = ex.getDescription();
    }
}
