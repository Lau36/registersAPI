package com.example.registers_api.exceptions;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private final String message;
    private final String details;
    private final LocalDateTime timestamp;

    public ExceptionResponse(String message, String details, LocalDateTime timestamp) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}