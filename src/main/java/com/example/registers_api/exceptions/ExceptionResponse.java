package com.example.registers_api.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExceptionResponse {
    private final String message;
    private final Integer details;
    private final LocalDateTime timestamp;

    public ExceptionResponse(String message, Integer details, LocalDateTime timestamp) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }


}