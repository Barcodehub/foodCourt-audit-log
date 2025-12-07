package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoDataFoundException(
            NoDataFoundException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.NOT_FOUND.value());
        response.put(ERROR, HttpStatus.NOT_FOUND.getReasonPhrase());
        response.put(MESSAGE, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Bad Request");
        response.put(MESSAGE, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());

        String message = exception.getMostSpecificCause().getMessage();
        HttpStatus status = HttpStatus.CONFLICT;

        if (message != null && (message.toLowerCase().contains("not null") || message.toLowerCase().contains("no nulo"))) {
            status = HttpStatus.BAD_REQUEST;
        }

        response.put(STATUS, status.value());
        response.put(ERROR, "Data Integrity Violation");
        response.put(MESSAGE, message);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.BAD_REQUEST.value());
        response.put(ERROR, "Validation Failed");
        response.put(MESSAGE, "Error de validación en los datos de entrada");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception exception) {
        Map<String, Object> response = new HashMap<>();
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put(ERROR, "Internal Server Error");
        response.put(MESSAGE, exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
