package org.eriksandsten.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(new GenericErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({Http400BadRequestException.class, Http405MethodNotAllowedRequestException.class})
    public ResponseEntity<?> handleBadRequestAndMethodNotAllowedException(HttpClientErrorException e) {
        return new ResponseEntity<>(new GenericErrorResponse(e.getStatusCode().value(), e.getStatusText()), e.getStatusCode());
    }

    // Handle @Valid errors (For @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(),
                e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage))), HttpStatus.BAD_REQUEST);
    }

    // Handle @Validated errors (For @RequestParam, @PathVariable)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        return new ResponseEntity<>(new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getConstraintViolations().stream()
                .collect(Collectors.toMap(violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage))), HttpStatus.BAD_REQUEST);
    }

    private static class ValidationErrorResponse {
        private final int status;
        private final Map<String, String> fieldErrors;

        public ValidationErrorResponse(int status, Map<String, String> fieldErrors) {
            this.status = status;
            this.fieldErrors = fieldErrors;
        }

        public int getStatus() {
            return status;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }
    }

    private static class GenericErrorResponse {
        private final int status;
        private final String message;

        public GenericErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
