package com.example.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.api.dto.SubscriptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<SubscriptionResponse> handleDuplicate(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new SubscriptionResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<SubscriptionResponse> handleInvalid(InvalidEmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SubscriptionResponse(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<SubscriptionResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SubscriptionResponse(e.getMessage()));
    }
}
