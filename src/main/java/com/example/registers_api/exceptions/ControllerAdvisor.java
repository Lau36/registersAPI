package com.example.registers_api.exceptions;

import com.example.registers_api.utils.ExceptionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {

    @ExceptionHandler(NotEmptyFieldException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyFieldException(NotEmptyFieldException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                ExceptionConstants.NOT_EMPTY_FIELDS,
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now())
        );
    }
}
