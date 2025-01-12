package com.example.registers_api.exceptions;

public class DoesntExistsException extends RuntimeException {
    public DoesntExistsException(String message) {
        super(message);
    }
}
