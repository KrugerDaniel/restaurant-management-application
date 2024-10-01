package com.reservation.restaurant.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

@AllArgsConstructor
public class ErrorsValidations {

    private String field;
    private String message;

    public ErrorsValidations(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

    public ErrorsValidations(ConstraintViolation<?> constraintViolation) {
        this(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
    }
}
