package com.dws.account.exception;

public class AccountNotCreatedException extends RuntimeException{
    AccountNotCreatedException(String message)
    {
        super(message);
    }
}
