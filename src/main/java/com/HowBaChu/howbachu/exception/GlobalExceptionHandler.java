package com.HowBaChu.howbachu.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> handlerCustomException(CustomException e) {
        log.error("CustomException: " + e.getErrorCode().getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getErrorCode().getStatus(), e.getMessage()), e.getErrorCode().getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException e, HttpHeaders h, HttpStatus s, WebRequest w
    ) {
        Map<String, String> validExceptions = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
            error -> {
                validExceptions.put(error.getField(), error.getDefaultMessage());
            }
        );
        log.error("Validation Exception: "+ validExceptions);
        return ResponseEntity.badRequest().body(validExceptions);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handlerException(Exception e) {
        log.error("Unexpected_Exception : " + e.getMessage());
        return ResponseEntity.status(500).body("UNEXPECTED_EXCEPTION: " + e);
    }
}
