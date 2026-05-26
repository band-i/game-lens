package io.bandi.gamelens.session.controller;

import io.bandi.gamelens.session.domain.model.Session;
import io.bandi.gamelens.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for session management.
 *
 * <p>Exposes endpoints to add, query, and update session from the user.
 */
@Tag(name = "Session", description = "User session management")
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Add a session of a game.
     *
     * <p>Returns {@code 201 Created} if new or {@code 404 Not Found} if the game does not
     * exist in game-service.
     *
     * @param rawgId RAWG ID of the game
     * @param notes  notes about the game
     */
    @Operation(
            summary = "Add a session of a game",
            description = """
                    Adds the session to the local.
                    Returns 201 if new, 404 if the game is not found in game-service.
                    """
    )
    @ApiResponse(responseCode = "201", description = "Session added successfully")
    @ApiResponse(responseCode = "404", description = "Game not found in game-service")
    @PostMapping
    public ResponseEntity<Session> saveSession(
            @RequestParam Long rawgId,
            @RequestParam(required = false) String notes
    ) {
        Session session = sessionService.saveSession(rawgId, notes);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }

    /**
     * Updates the end of a session entry.
     *
     * <p>Returns {@code 409 Conflict} if the session already ended
     * or {@code 404 Not Found} if no entry exists for that session.
     *
     * @param sessionId ID of the session
     */
    @Operation(summary = "Update session end", description = "Updates the ended date of an existing session entry.")
    @ApiResponse(responseCode = "200", description = "Ended at field updated")
    @ApiResponse(responseCode = "409", description = "Session already ended")
    @ApiResponse(responseCode = "404", description = "No session entry for that ID")
    @PatchMapping(value = "/{sessionId}")
    public ResponseEntity<Session> updateSession(
            @PathVariable Long sessionId
    ) {
        return new ResponseEntity<>(sessionService.updateSession(sessionId), HttpStatus.OK);
    }

    /**
     * Returns all entries in the session.
     */
    @Operation(summary = "Get all session entries", description = "Returns all session in the local catalog.")
    @ApiResponse(responseCode = "200", description = "List of session entries, empty if none exist yet")
    @GetMapping
    public ResponseEntity<List<Session>> getAllSessions() {
        return new ResponseEntity<>(sessionService.getAllSessions(), HttpStatus.OK);

    }

    /**
     * Returns a session entry by ID.
     *
     * <p>Returns {@code 404 Not Found} if no entry exists for that session.
     *
     * @param sessionId ID of the session
     */
    @Operation(summary = "Get a session entry by ID", description = "Returns the session entry for the given ID.")
    @ApiResponse(responseCode = "200", description = "Entry found")
    @ApiResponse(responseCode = "404", description = "No session entry for that ID")
    @GetMapping(value = "/{sessionId}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long sessionId) {
        return new ResponseEntity<>(sessionService.getSessionById(sessionId), HttpStatus.OK);
    }
}
