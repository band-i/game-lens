package io.bandi.gamelens.backlog.service;

import io.bandi.gamelens.backlog.client.BacklogClient;
import io.bandi.gamelens.backlog.domain.dto.BacklogRequest;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.domain.model.Status;
import io.bandi.gamelens.backlog.repository.BacklogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Core service for backlog management.
 *
 * <p>Handles persistence of backlog entries and delegates game existence
 * checks to game-service via {@link BacklogClient}.
 */
@Service
public class BacklogService {

    private final BacklogClient backlogClient;
    private final BacklogRepository backlogRepository;

    public BacklogService(BacklogClient backlogClient, BacklogRepository backlogRepository) {
        this.backlogClient = backlogClient;
        this.backlogRepository = backlogRepository;
    }

    /**
     * Creates a new backlog entry for the given game with {@code PENDING} status.
     *
     * @param gameId   RAWG ID of the game to add
     * @param priority optional priority value, {@code null} if not provided
     * @return the persisted {@link Backlog} entry
     */
    public Backlog saveBacklog(Long gameId, Integer priority) {
        Backlog backlog = new Backlog();
        backlog.setGameId(gameId);
        backlog.setStatus(Status.PENDING);
        backlog.setPriority(priority);
        backlog.setCreatedAt(LocalDateTime.now());
        backlog.setUpdatedAt(LocalDateTime.now());

        return backlogRepository.save(backlog);
    }

    /**
     * Updates the status of an existing backlog entry.
     *
     * @param gameId         RAWG ID of the game
     * @param backlogRequest request body containing the new status
     * @return the updated {@link Backlog} entry
     */
    public Backlog updateBacklog(Long gameId, BacklogRequest backlogRequest) {
        Backlog backlog = getBacklogById(gameId);
        backlog.setStatus(backlogRequest.status());
        backlog.setPriority(backlogRequest.priority());

        return backlogRepository.save(backlog);
    }

    /**
     * Deletes a backlog entry by its internal ID.
     *
     * <p>Idempotent — does nothing if the entry does not exist.
     *
     * @param id internal backlog ID
     */
    public void deleteBacklog(Long id) {
        backlogRepository.deleteById(id);
    }

    /**
     * Checks whether a game exists in game-service.
     *
     * <p>Returns {@code false} if game-service is unreachable or returns an error.
     *
     * @param gameId RAWG ID of the game
     * @return {@code true} if game-service finds the game
     */
    public boolean gameExists(Long gameId) {
        try {
            return backlogClient.getResponse(gameId) != null;
        } catch (RestClientException e) {
            return false;
        }
    }

    /**
     * Checks whether a backlog entry exists for the given game.
     *
     * @param gameId RAWG ID of the game
     * @return {@code true} if an entry already exists
     */
    public boolean backlogExists(Long gameId) {
        return backlogRepository.existsByGameId(gameId);
    }

    /**
     * Retrieves a backlog entry by game ID.
     *
     * <p>Call {@link #backlogExists(Long)} first — returns {@code null} if not found.
     *
     * @param gameId RAWG ID of the game
     * @return the matching {@link Backlog}, or {@code null} if not found
     */
    public Backlog getBacklogById(Long gameId) {
        return backlogRepository.findByGameId(gameId);
    }

    /**
     * Returns all backlog entries.
     *
     * @return list of entries, empty if none exist yet
     */
    public List<Backlog> getAllBacklogs() {
        return backlogRepository.findAll();
    }
}
