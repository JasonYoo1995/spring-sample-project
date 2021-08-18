package com.konkuk.sample.exception;

public class ZeroBalanceException extends RuntimeException{
    public ZeroBalanceException() {
    }
    public ZeroBalanceException(String message) {
        super(message);
    }
    public ZeroBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ZeroBalanceException(Throwable cause) {
        super(cause);
    }
}
