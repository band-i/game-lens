package io.bandi.gamelens.game.service;

import io.bandi.gamelens.game.client.GameClient;
import io.bandi.gamelens.game.domain.dto.RawgGameDto;
import io.bandi.gamelens.game.domain.model.Game;
import io.bandi.gamelens.game.exception.GameAlreadyExistsException;
import io.bandi.gamelens.game.exception.GameNotFoundException;
import io.bandi.gamelens.game.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Core service for game catalog management.
 *
 * <p>Handles RAWG API lookups and local persistence. Acts as the single
 * source of truth for game data within this service.
 */
@Service
public class GameService {

    private final GameClient gameClient;
    private final GameRepository gameRepository;

    public GameService(GameClient gameClient, GameRepository gameRepository) {
        this.gameClient = gameClient;
        this.gameRepository = gameRepository;
    }

    /**
     * Searches RAWG by name and persists the first result.
     *
     * <p>Only the first platform in the list is stored. Falls back to
     * {@code "Unknown"} if the platforms list is empty.
     *
     * @param name game title to search
     * @return the persisted {@link Game} entity
     * @throws GameNotFoundException if RAWG returns no results for that name
     * @throws GameAlreadyExistsException if the game is already in the local catalog
     */
    public Game saveGame(String name) {
        RawgGameDto searchedGame = getInfoGame(name);
        if (searchedGame == null) {
            throw new GameNotFoundException(name);
        }

        if (gameExists(searchedGame.id())) {
            throw new GameAlreadyExistsException(name, searchedGame.id());
        }

        Game game = new Game();
        game.setRawgId(searchedGame.id());
        game.setTitle(searchedGame.name());
        game.setCoverUrl(searchedGame.backgroundImage());
        game.setAveragePlaytime(searchedGame.playtime());
        game.setRating(searchedGame.rating());
        game.setReleased(searchedGame.released());
        game.setPlatform(searchedGame.platforms()
                .stream()
                .findFirst()
                .map(rawgPlatformWrapper ->
                        rawgPlatformWrapper.platform().name())
                .orElse("Unknown"));

        return gameRepository.save(game);
    }

    /**
     * Queries RAWG for the given name and returns the first match.
     *
     * @param name game title to search
     * @return the first result, or {@code null} if RAWG returns no results
     */
    public RawgGameDto getInfoGame(String name) {
        List<RawgGameDto> results = gameClient.getResponse(name).results();

        return results.stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks whether a game with the given RAWG ID already exists locally.
     *
     * @param rawgId the RAWG identifier
     * @return {@code true} if the game is already persisted
     */
    public boolean gameExists(Long rawgId) {
        return gameRepository.existsByRawgId(rawgId);
    }

    /**
     * Retrieves a game by its RAWG ID.
     *
     * @param rawgId the RAWG identifier
     * @return the matching {@link Game}
     * @throws GameNotFoundException if no game is found with that RAWG ID
     */
    public Game getGameById(Long rawgId) {
        if (!gameExists(rawgId)) {
            throw new GameNotFoundException(rawgId);
        }
        return gameRepository.findByRawgId(rawgId);
    }

    /**
     * Returns all games currently stored in the local catalog.
     *
     * @return list of games, empty if none have been saved yet
     */
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
}
