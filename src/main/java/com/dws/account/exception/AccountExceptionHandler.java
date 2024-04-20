package com.dws.account.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AccountExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = InsufficientBalanceException.class)
    public ResponseEntity<Object> handleInsufficientExceptions(final InsufficientBalanceException e, final WebRequest request) {
        Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
        return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    @ExceptionHandler(value = AccountNotCreatedException.class)
    public ResponseEntity<Object> handleAccountExceptions(final AccountNotCreatedException e, final WebRequest request) {
        Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
        return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleInsufficientBalanceException(final AccountNotFoundException e,final WebRequest request) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<Object> handleDuplicateAccountException(DuplicateAccountException e,final WebRequest request) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e, final WebRequest request) {
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
        return handleExceptionInternal(e, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

