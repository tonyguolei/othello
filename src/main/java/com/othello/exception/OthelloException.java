package com.othello.exception;

public class OthelloException extends Exception {
    public OthelloException() {
    }

    public OthelloException(String message) {
        super(message);
    }

    public OthelloException(String message, Throwable cause) {
        super(message, cause);
    }

    public OthelloException(Throwable cause) {
        super(cause);
    }

    public OthelloException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
