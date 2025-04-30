package br.ufpb.dcx.apps4society.meuguiapbapi.exception;

import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ResourceExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ResourceExceptionHandler.class);

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFoundException(ObjectNotFoundException e, HttpServletRequest request) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "recurso não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );

        log.info("Recurso não encontrado: {}", error.getMessage());
        log.debug("Request responsavel: {}", request);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro de integridade de dados",
                e.getMostSpecificCause().getMessage(),
                request.getRequestURI()
        );

        log.info("Violação da integridade de dados: {}", error.getMessage());
        log.debug("Request responsavel: {}", request);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<StandardError> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e, HttpServletRequest request) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "recurso já existe",
                e.getLocalizedMessage(),
                request.getRequestURI()
        );

        log.info("Recurso já existe: {}", error.getMessage());
        log.debug("Request responsavel: {}", request);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<StandardError> handleValidationException(ValidationException e, HttpServletRequest request) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "erro de validação",
                e.getLocalizedMessage(),
                request.getRequestURI()
        );

        log.info("Erro de validação: {}", error.getMessage());
        log.debug("Request responsavel: {}", request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<StandardError> handleEmailAlreadyInUseException(EmailAlreadyInUseException e, HttpServletRequest request) {
        StandardError error = new StandardError(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "conflito",
                e.getLocalizedMessage(),
                request.getRequestURI()
        );

        log.info("Email já em uso: {}", error.getMessage());
        log.debug("Request responsavel: {}", request);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Error handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.info("Erro de validação: {}", e.getMessage());
        log.debug("Request responsavel: {}", request);

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
