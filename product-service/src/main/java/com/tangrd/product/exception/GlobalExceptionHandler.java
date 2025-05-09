package com.tangrd.product.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.NOT_FOUND.value(),
      ex.getMessage(),
      LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.CONFLICT.value(),
      ex.getMessage(),
      LocalDateTime.now()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Data validation failed",
      LocalDateTime.now(),
      errors
    );
    return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
  }
//
//  @ExceptionHandler(AccessDeniedException.class)
//  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
//    ErrorResponse errorResponse = new ErrorResponse(
//      HttpStatus.FORBIDDEN.value(),
//      "Access denied",
//      LocalDateTime.now()
//    );
//    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//  }
//
//  @ExceptionHandler(Exception.class)
//  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
//    ErrorResponse errorResponse = new ErrorResponse(
//      HttpStatus.INTERNAL_SERVER_ERROR.value(),
//      "Internal server error",
//      LocalDateTime.now()
//    );
//    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//  }

  @Data
  @AllArgsConstructor
  static class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
  }

  @Getter
  @Setter
  static class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {
      super(status, message, timestamp);
      this.errors = errors;
    }
  }
} 
