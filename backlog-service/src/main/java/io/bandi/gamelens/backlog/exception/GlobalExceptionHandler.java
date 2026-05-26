package io.bandi.gamelens.backlog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception handler for backlog-service.
 *
 * <p>Maps domain exceptions to the appropriate HTTP responses
 * so controllers stay free of error-handling logic.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleGameNotFound(GameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameAlreadyInBacklogException.class)
    public ResponseEntity<String> handleAlreadyInBacklog(GameAlreadyInBacklogException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BacklogNotFoundException.class)
    public ResponseEntity<String> handleGameNotFound(BacklogNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
