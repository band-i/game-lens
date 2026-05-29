package io.bandi.gamelens.session.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(Long sessionId) {
        super("Session not found with id: %d".formatted(sessionId));
    }
}
