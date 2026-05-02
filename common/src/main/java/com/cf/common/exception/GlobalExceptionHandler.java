package com.cf.common.exception;

import com.cf.common.exception.BaseException;
import com.cf.common.exception.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException exception) {
        ErrorResponse error = new ErrorResponse(exception.getMessage(), exception.getStatus().value(), LocalDateTime.now());
        return new ResponseEntity<>(error, exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()).
                findFirst().orElse("Validation error");

        ErrorResponse error = new ErrorResponse(errorMessage, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception exception) {
        ErrorResponse error = new ErrorResponse("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolate(DataIntegrityViolationException exception) {
        ErrorResponse error = new ErrorResponse("Data integrity violation", HttpStatus.CONTINUE.value(), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}