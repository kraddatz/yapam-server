package me.raddatz.yapam.common.error;

import lombok.Getter;

@Getter
public class YapamException extends RuntimeException {

    private final String error;

    public YapamException() {
        super();
        this.error = this.getClass().getSimpleName();
    }

}
