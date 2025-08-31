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
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now())
        );
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now())
        );
    }

    @ExceptionHandler(MaxLengthExceededException.class)
    public ResponseEntity<ExceptionResponse> handleMaxLengthExceededException(MaxLengthExceededException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                         e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now())
        );
    }

    @ExceptionHandler(DoesntExistsException.class)
    public ResponseEntity<ExceptionResponse> handleDoesntExistsException(DoesntExistsException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now())
        );
    }

    @ExceptionHandler(ErrorUserCreation.class)
    public ResponseEntity<ExceptionResponse> handleErrorUserCreation(ErrorUserCreation e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                new ExceptionResponse(
                        e.getMessage(),
                        e.getStatusCode(),
                        LocalDateTime.now()
                )
        );
    }

    @ExceptionHandler(ErrorWithKeycloakException.class)
    public ResponseEntity<ExceptionResponse> handlErrorWithKeycloakException(ErrorWithKeycloakException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        LocalDateTime.now())
        );
    }

    @ExceptionHandler(NotEnabledException.class)
    public ResponseEntity<ExceptionResponse> handleNotEnabledException(NotEnabledException e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now())
        );
    }

    @ExceptionHandler(DoesntHavePermissions.class)
    public ResponseEntity<ExceptionResponse> handleDoesntHavePermissions(DoesntHavePermissions e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now())
        );
    }

    @ExceptionHandler(UserDoesntExists.class)
    public ResponseEntity<ExceptionResponse> userDoesntExists(UserDoesntExists e) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now())
        );
    }
}
