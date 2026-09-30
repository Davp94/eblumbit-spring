package com.blumbit.eblumbit.exception;

public class ConfictException extends DomainException {

    public ConfictException(String message) {
        super(message, 409, "CONFLICT");
    }

}
