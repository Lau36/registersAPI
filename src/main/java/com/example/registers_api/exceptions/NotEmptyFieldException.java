package com.example.registers_api.exceptions;

public class NotEmptyFieldException extends RuntimeException {
    public NotEmptyFieldException(String message) {
        super(message);
    }
}
