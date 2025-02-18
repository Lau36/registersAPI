package com.example.registers_api.exceptions;

public class ErrorWithKeycloakException extends RuntimeException {
    public ErrorWithKeycloakException(String message) {
        super(message);
    }
}
