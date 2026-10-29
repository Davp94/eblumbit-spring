package com.blumbit.eblumbit.exception;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blumbit.eblumbit.common.dto.CustomErrorResponse;

import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception exception, HttpServletRequest req) {
        log.error("STANDARD EXCEPTION: {}", exception.getMessage(), exception);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno de servidor",
                exception.getMessage(), req);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationException(ValidationException exception,
            HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Error de validacion", exception.getMessage(), req);
    }

    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleSpringAuthenticationException(
            org.springframework.security.core.AuthenticationException exception, HttpServletRequest req) {
        log.warn("AUTHENTICATION EXCEPTION: {}", exception.getMessage());
        return build(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_EXCEPTION", "Credenciales inválidas", req);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException exception, HttpServletRequest req) {
        log.warn("ACCESS DENIED: {}", exception.getMessage());
        return build(HttpStatus.FORBIDDEN, "FORBIDDEN", "Acceso denegado", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpServletRequest req) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Error de validación");
        return build(HttpStatus.BAD_REQUEST, "Error de validacion", message, req);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<CustomErrorResponse> handleDomainException(DomainException exception, HttpServletRequest req) {
        log.error("DOMAIN EXCEPTION: {}", exception.getMessage(), exception);
        HttpStatus status = HttpStatus.resolve(exception.getStatusCode());
        if (status == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return build(status, exception.getErrorCode(), exception.getMessage(), req);
    }

    private ResponseEntity<CustomErrorResponse> build(HttpStatus status, String title, String detail,
            HttpServletRequest req) {
        String traceId = req.getHeader("X-Request-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }
        CustomErrorResponse body = CustomErrorResponse.builder()
                .title(title)
                .status(status.value())
                .detail(detail)
                .instance(req.getRequestURI())
                .traceId(traceId)
                .build();
        log.debug("Error response {} at {} - {}", status.value(), req.getRequestURI(), new Date());
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}
