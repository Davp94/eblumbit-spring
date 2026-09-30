package com.blumbit.eblumbit.exception;

import lombok.Getter;

@Getter 
public abstract class DomainException extends Exception {

    private int statusCode;
    private String errorCode;

    public DomainException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
}
