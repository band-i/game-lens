package io.bandi.gamelens.session.exception;

public class SessionEndedException extends RuntimeException {
    public SessionEndedException(Long sessionId) {
        super("Session already ended with id: %d".formatted(sessionId));
    }
}
