package com.example.vehicles.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class InputValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public InputValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

}
