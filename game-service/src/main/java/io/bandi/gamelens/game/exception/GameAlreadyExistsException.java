package io.bandi.gamelens.game.exception;

public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String name, Long gameId) {
        super("Game %s already exists id: %d".formatted(name, gameId));
    }
}
