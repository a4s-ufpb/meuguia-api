package br.ufpb.dcx.apps4society.meuguiapbapi.exception;

import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.ServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

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


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error handleMethodArgumentNotValidException(MethodArgumentNotValidException e, ServletRequest request) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return processFieldErrors(fieldErrors);
    }

    private Error processFieldErrors(List<FieldError> fieldErrors) {
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), "Validation error");
        for (FieldError fieldError: fieldErrors) {
            error.addFieldError(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
        }
        return error;
    }

    public static class Error {
        private final int status;
        private final String message;
        private final Long timeStamp = System.currentTimeMillis();
        private final List<FieldError> fieldErrors;

        Error(int status, String message) {
            this.status = status;
            this.message = message;
            fieldErrors = new ArrayList<>();
        }

        public void addFieldError(String objectName, String path, String message) {
            FieldError error = new FieldError(objectName, path, message);
            fieldErrors.add(error);
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Long getTimeStamp() {
            return timeStamp;
        }

        public List<FieldError> getFieldErrors() {
            return fieldErrors;
        }
    }
}
