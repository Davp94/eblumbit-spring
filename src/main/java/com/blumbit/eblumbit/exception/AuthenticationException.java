package com.blumbit.eblumbit.exception;

public class AuthenticationException extends DomainException{

    public AuthenticationException(String message) {
        super(message, 401, "AUTHENTICATION_EXCEPTION");
    }

}
