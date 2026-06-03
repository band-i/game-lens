package io.bandi.gamelens.backlog.controller;

import io.bandi.gamelens.backlog.domain.dto.BacklogRequest;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.service.BacklogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for backlog management.
 *
 * <p>Exposes endpoints to add, query, update, and remove games from the user's backlog.
 */
@Tag(name = "Backlog", description = "User backlog management and game status tracking")
@RestController
@RequestMapping("/api/v1/backlog")
public class BacklogController {

    private final BacklogService backlogService;

    public BacklogController(BacklogService backlogService) {
        this.backlogService = backlogService;
    }

    /**
     * Adds a game to the backlog.
     *
     * <p>Returns {@code 201 Created} if new, {@code 409 Conflict} if already in the backlog,
     * or {@code 404 Not Found} if the game does not exist in game-service.
     *
     * @param gameId   RAWG ID of the game
     * @param priority priority value
     */
    @Operation(
            summary = "Add a game to the backlog",
            description = """
                    Adds the game to the backlog with PENDING status.
                    Returns 201 if new, 409 if already exists, 404 if the game is not found in game-service.
                    """
    )
    @ApiResponse(responseCode = "201", description = "Game added to backlog")
    @ApiResponse(responseCode = "409", description = "Game already in backlog")
    @ApiResponse(responseCode = "404", description = "Game not found in game-service")
    @PostMapping
    public ResponseEntity<Backlog> saveBacklog(
            @RequestParam Long gameId,
            @RequestParam Integer priority
    ) {
        Backlog backlog = backlogService.saveBacklog(gameId, priority);
        return new ResponseEntity<>(backlog, HttpStatus.CREATED);
    }

    /**
     * Returns all entries in the backlog.
     */
    @Operation(summary = "Get all backlog entries", description = "Returns all entries in the user's backlog.")
    @ApiResponse(responseCode = "200", description = "List of backlog entries, empty if none exist yet")
    @GetMapping
    public ResponseEntity<List<Backlog>> getAllBacklogs() {
        return new ResponseEntity<>(backlogService.getAllBacklogs(), HttpStatus.OK);

    }

    /**
     * Returns a backlog entry by game ID.
     *
     * <p>Returns {@code 404 Not Found} if no entry exists for that game.
     *
     * @param gameId RAWG ID of the game
     */
    @Operation(summary = "Get a backlog entry by game ID", description = "Returns the backlog entry for the given game.")
    @ApiResponse(responseCode = "200", description = "Entry found")
    @ApiResponse(responseCode = "404", description = "No backlog entry for that game")
    @GetMapping(value = "/{gameId}")
    public ResponseEntity<Backlog> getBacklogById(@PathVariable Long gameId) {
        return new ResponseEntity<>(backlogService.getBacklogById(gameId), HttpStatus.OK);
    }

    /**
     * Updates the status of a backlog entry.
     *
     * <p>Returns {@code 404 Not Found} if no entry exists for that game.
     *
     * @param gameId         RAWG ID of the game
     * @param backlogRequest request body with the new status
     */
    @Operation(summary = "Update backlog status", description = "Updates the status of an existing backlog entry.")
    @ApiResponse(responseCode = "200", description = "Status updated")
    @ApiResponse(responseCode = "404", description = "No backlog entry for that game")
    @PatchMapping(value = "/{gameId}")
    public ResponseEntity<Backlog> updateBacklog(
            @PathVariable Long gameId,
            @RequestBody BacklogRequest backlogRequest
    ) {
        return new ResponseEntity<>(backlogService.updateBacklog(gameId, backlogRequest), HttpStatus.OK);
    }

    /**
     * Removes a backlog entry by its internal ID.
     *
     * <p>Idempotent — always returns {@code 204 No Content}.
     *
     * @param id internal backlog ID
     */
    @Operation(summary = "Remove a backlog entry", description = "Deletes a backlog entry by its internal ID. Always returns 204.")
    @ApiResponse(responseCode = "204", description = "Entry deleted or did not exist")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBacklog(@PathVariable Long id) {
        backlogService.deleteBacklog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
