package br.ufpb.dcx.apps4society.meuguiapbapi.exception;

import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.ServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException e, ServletRequest request) {
        StandardError error = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrityViolation(DataIntegrityViolationException e, ServletRequest request) {
        String errorMessage = "Duplicate data error: " + e.getMostSpecificCause().getMessage();
        StandardError error = new StandardError(
                HttpStatus.NOT_FOUND.value(),
                errorMessage,
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> handleValidationException(ValidationException e, ServletRequest request) {
        String errorMessage = "Validation error: " + e.getLocalizedMessage();
        StandardError error = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, ServletRequest request) {
        String errorMessage = "Validation error: " + e.getLocalizedMessage();
        StandardError error = new StandardError(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
