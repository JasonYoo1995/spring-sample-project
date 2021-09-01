package com.konkuk.sample.aop;

import com.konkuk.sample.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        log.error("INTERNAL_SERVER_ERROR : " + ex.getMessage() + " | " + request.getDescription(true));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccountNotFoundException.class, MemberNotFoundException.class})
    public final ResponseEntity<Object> handleAccountNotFoundException(Exception ex, WebRequest request){
        log.error("NOT FOUND : " + ex.getMessage() + " | " + request.getDescription(true));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptySessionException.class)
    public final ResponseEntity<Object> handleEmptySessionException(Exception ex, WebRequest request){
        log.error("UNAUTHORIZED : " + ex.getMessage() + " | " + request.getDescription(true));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ZeroBalanceException.class)
    public final ResponseEntity<Object> handleZeroBalanceException(Exception ex, WebRequest request){
        log.error("FORBIDDEN : " + ex.getMessage() + " | " + request.getDescription(true));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage());
        return new ResponseEntity(exceptionResponse, HttpStatus.FORBIDDEN);
    }
}
