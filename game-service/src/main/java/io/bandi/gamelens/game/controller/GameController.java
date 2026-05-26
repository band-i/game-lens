package io.bandi.gamelens.game.controller;

import io.bandi.gamelens.game.domain.model.Game;
import io.bandi.gamelens.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for the game catalog.
 *
 * <p>Exposes endpoints to search and persist games from RAWG,
 * and to query the local catalog.
 */
@Tag(name = "Games", description = "Game catalog management and RAWG integration")
@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Searches RAWG by name and persists the first result if not already stored.
     *
     * <p>Returns {@code 201 Created} if the game is new, {@code 409 Conflict} if it
     * already exists locally, or {@code 404 No Content} if RAWG finds nothing.
     *
     * @param name game title to search
     */
    @Operation(
            summary = "Search and save a game",
            description = """
                    Searches RAWG by name and persists the first result.
                    Returns 201 if new, 409 if already exists, 404 if RAWG finds nothing.
                    """
    )
    @ApiResponse(responseCode = "201", description = "Game saved successfully")
    @ApiResponse(responseCode = "409", description = "Game already exists in local catalog")
    @ApiResponse(responseCode = "404", description = "No results found in RAWG")
    @PostMapping
    public ResponseEntity<Game> searchAndSave(@RequestParam String name) {
        Game game = gameService.saveGame(name);
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    }

    /**
     * Returns a game from the local catalog by its RAWG ID.
     *
     * <p>Returns {@code 404 Not Found} if the game has not been saved yet.
     *
     * @param rawgId the RAWG identifier
     */
    @Operation(
            summary = "Get a game by RAWG ID",
            description = "Returns a game from the local catalog by its RAWG ID."
    )
    @ApiResponse(responseCode = "200", description = "Game found")
    @ApiResponse(responseCode = "404", description = "Game not found in local catalog")
    @GetMapping(value = "/{rawgId}")
    public ResponseEntity<Game> searchGame(@PathVariable("rawgId") Long rawgId) {
        return new ResponseEntity<>(gameService.getGameById(rawgId), HttpStatus.OK);

    }

    /**
     * Returns all games in the local catalog.
     */
    @Operation(summary = "Get all games", description = "Returns all games in the local catalog.")
    @ApiResponse(responseCode = "200", description = "List of games, empty if none saved yet")
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        return new ResponseEntity<>(gameService.getAllGames(), HttpStatus.OK);
    }
}
