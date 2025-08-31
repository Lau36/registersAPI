package com.example.registers_api.exceptions;

public class UserDoesntExists extends RuntimeException {
    public UserDoesntExists(String message) {
        super(message);
    }
}
