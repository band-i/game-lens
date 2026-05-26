package io.bandi.gamelens.session.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long gameId) {
        super("Game not found with id: %d".formatted(gameId));
    }
}
