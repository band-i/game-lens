package io.bandi.gamelens.game.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String gameName) {
        super("Game not found in rawg: %s".formatted(gameName));
    }

    public GameNotFoundException(Long rawgId) {
        super("Game not found in local catalog: %d".formatted(rawgId));
    }
}
