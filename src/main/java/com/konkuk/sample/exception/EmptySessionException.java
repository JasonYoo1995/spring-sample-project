package com.konkuk.sample.exception;

public class EmptySessionException extends RuntimeException{
    public EmptySessionException() {
    }
    public EmptySessionException(String message) {
        super(message);
    }
    public EmptySessionException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmptySessionException(Throwable cause) {
        super(cause);
    }
}
