package com.luizjacomn.designpatterns.exception;

import java.io.Serial;

public class InvalidLevelTransitionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4059816894503473910L;

    public InvalidLevelTransitionException(String message) {
        super(message);
    }

}
