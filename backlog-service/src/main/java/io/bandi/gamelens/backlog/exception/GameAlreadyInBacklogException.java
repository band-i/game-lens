package io.bandi.gamelens.backlog.exception;

public class GameAlreadyInBacklogException extends RuntimeException {
    public GameAlreadyInBacklogException(Long gameId) {
        super("Game already exists id: %d".formatted(gameId));
    }
}
