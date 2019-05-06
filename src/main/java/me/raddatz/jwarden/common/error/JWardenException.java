package me.raddatz.jwarden.common.error;

import lombok.Getter;

@Getter
public class JWardenException extends RuntimeException {

    private final String description;

    public JWardenException() {
        super();
        this.description = this.getClass().getSimpleName();
    }

}
