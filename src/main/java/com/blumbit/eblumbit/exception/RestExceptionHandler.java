package com.blumbit.eblumbit.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blumbit.eblumbit.common.dto.CustomErrorResponse;

import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice 
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<CustomErrorResponse<String>> handleException(Exception exception, HttpServletRequest req) {
        return new ResponseEntity<>(CustomErrorResponse.<String>builder()
        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
        .message(exception.getMessage())
        .timestamp(new Date().toString())
        .path(req.getRequestURI())
        .build(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CustomErrorResponse<String>> handleValidationException(ValidationException exception, HttpServletRequest req) {
        return new ResponseEntity<>(CustomErrorResponse.<String>builder()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .errorCode(HttpStatus.BAD_REQUEST.name())
        .message(exception.getMessage())
        .timestamp(new Date().toString())
        .path(req.getRequestURI())
        .build(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
