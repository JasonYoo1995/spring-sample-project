package com.konkuk.sample.exception;

public class NoAccountFoundException extends RuntimeException{
    public NoAccountFoundException() {
    }
    public NoAccountFoundException(String message) {
        super(message);
    }
    public NoAccountFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoAccountFoundException(Throwable cause) {
        super(cause);
    }
}
