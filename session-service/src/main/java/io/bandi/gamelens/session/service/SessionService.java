package io.bandi.gamelens.session.service;

import io.bandi.gamelens.session.client.SessionClient;
import io.bandi.gamelens.session.domain.model.Session;
import io.bandi.gamelens.session.exception.GameNotFoundException;
import io.bandi.gamelens.session.exception.SessionEndedException;
import io.bandi.gamelens.session.exception.SessionNotFoundException;
import io.bandi.gamelens.session.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Core service for session management.
 *
 * <p>Handles persistence of session entries and delegates game existence
 * checks to game-service via {@link SessionClient}.
 */
@Service
public class SessionService {

    private final SessionClient sessionClient;
    private final SessionRepository sessionRepository;

    public SessionService(SessionClient sessionClient, SessionRepository sessionRepository) {
        this.sessionClient = sessionClient;
        this.sessionRepository = sessionRepository;
    }


    /**
     * Creates a new session entry for the given game.
     *
     * @param gameId RAWG ID of the game to add
     * @param notes  notes about the game
     * @return the persisted {@link Session} entry
     * @throws GameNotFoundException if game not exists in game-service
     */
    public Session saveSession(Long gameId, String notes) {
        if (!gameExists(gameId)) {
            throw new GameNotFoundException(gameId);
        }

        Session session = new Session();
        session.setGameId(gameId);
        session.setNotes(notes);

        return sessionRepository.save(session);
    }

    /**
     * Updates the ended at field of an existing session entry.
     *
     * @param sessionId ID of the session
     * @return the updated {@link Session} entry
     * @throws SessionNotFoundException if no entry exists for that game
     */
    public Session updateSession(Long sessionId) {
        Session session = getSessionById(sessionId);

        if (session.getEndedAt() != null) {
            throw new SessionEndedException(sessionId);
        }
        session.setEndedAt(LocalDateTime.now());

        return sessionRepository.save(session);
    }

    public Session getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
    }

    /**
     * Returns all session entries.
     *
     * @return list of sessions, empty if none exist yet
     */
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
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
            return sessionClient.getResponse(gameId) != null;
        } catch (RestClientException e) {
            return false;
        }
    }

}
