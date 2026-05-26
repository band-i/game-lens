package io.bandi.gamelens.backlog.exception;

public class BacklogNotFoundException extends RuntimeException {
    public BacklogNotFoundException(Long gameId) {
        super("Backlog not found with game id: %d".formatted(gameId));
    }
}
