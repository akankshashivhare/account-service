package com.dws.account.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message)
    {
        super(message);
    }
}
