package com.example.employee_mangement.share;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionResponse {
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<GlobalResponse<?>> handleNoResourceFoundException(NoResourceFoundException ex) {
        var error = List.of(
            new GlobalResponse.ErrorItems("Resource not found")
        );

        return ResponseEntity.status(404).body(new GlobalResponse<>(error));
    }

    @ExceptionHandler(CustomResponseException.class)
    public ResponseEntity<GlobalResponse<?>> handleCustomResponseException(CustomResponseException ex) {
        var error = List.of(
            new GlobalResponse.ErrorItems(ex.getMessage())
        );

        return ResponseEntity.status(ex.getStatus()).body(new GlobalResponse<>(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var error = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new GlobalResponse.ErrorItems(fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity.status(400).body(new GlobalResponse<>(error));
    }
}
