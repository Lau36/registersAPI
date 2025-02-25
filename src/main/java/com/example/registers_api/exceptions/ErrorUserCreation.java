package com.example.registers_api.exceptions;
public class ErrorUserCreation extends RuntimeException {
    private final int statusCode;

    public ErrorUserCreation(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
