package com.example.registers_api.exceptions;

public class MaxLengthExceededException extends RuntimeException {
    public MaxLengthExceededException(String message) {
        super(message);
    }
}
