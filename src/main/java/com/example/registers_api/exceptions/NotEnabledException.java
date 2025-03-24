package com.example.registers_api.exceptions;

public class NotEnabledException extends RuntimeException {
    public NotEnabledException(String message) {
        super(message);
    }
}
