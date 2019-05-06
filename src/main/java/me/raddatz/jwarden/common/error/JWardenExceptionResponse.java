package me.raddatz.jwarden.common.error;

import lombok.Getter;

@Getter
public class JWardenExceptionResponse {

    private String description;

    public JWardenExceptionResponse(JWardenException ex) {
        this.description = ex.getDescription();
    }
}
