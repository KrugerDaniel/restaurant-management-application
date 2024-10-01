package com.reservation.restaurant.infrastructure.exceptions;

import com.reservation.restaurant.exceptions.ErrorsValidations;
import com.reservation.restaurant.exceptions.NotFoundException;
import com.reservation.restaurant.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> validationException(ValidationException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorsValidations>> fieldValidation(MethodArgumentNotValidException exception) {
        final List<FieldError> fieldErrors = exception.getFieldErrors();

        return ResponseEntity.badRequest().body(fieldErrors.stream().map(ErrorsValidations::new).toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorsValidations>> constraintValidation(ConstraintViolationException exception) {
        final Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        return ResponseEntity.badRequest().body(violations.stream().map(ErrorsValidations::new).toList());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> notFoundException() {
        return ResponseEntity.notFound().build();
    }
}
