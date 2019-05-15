package me.raddatz.yapam.common.error;

import lombok.Getter;

@Getter
public class YapamException extends RuntimeException {

    private final String description;

    public YapamException() {
        super();
        this.description = this.getClass().getSimpleName();
    }

}
