package com.example.registers_api.exceptions;

public class DoesntHavePermissions extends RuntimeException {
    public DoesntHavePermissions(String message) {
        super(message);
    }
}
