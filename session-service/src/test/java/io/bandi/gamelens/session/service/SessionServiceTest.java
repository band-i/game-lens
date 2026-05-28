package io.bandi.gamelens.session.service;

import io.bandi.gamelens.session.client.SessionClient;
import io.bandi.gamelens.session.domain.dto.GameServiceResponse;
import io.bandi.gamelens.session.domain.model.Session;
import io.bandi.gamelens.session.exception.GameNotFoundException;
import io.bandi.gamelens.session.exception.SessionEndedException;
import io.bandi.gamelens.session.exception.SessionNotFoundException;
import io.bandi.gamelens.session.repository.SessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionClient sessionClient;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session buildSession(Long gameId, String notes) {
        Session session = new Session();
        session.setGameId(gameId);
        session.setNotes(notes);

        return session;
    }

    @Test
    @DisplayName("saveSession: persists session")
    void saveSession_persistsWithPendingStatus() {
        Session saved = buildSession(1L, "Good Game");
        when(sessionRepository.save(any(Session.class)))
                .thenReturn(saved);

        when(sessionClient.getResponse(1L)).thenReturn(new GameServiceResponse(
                1L, 1L, "Hades", "http://cover.jpg", 20, null, "PC", null
        ));

        Session result = sessionService.saveSession(1L, "Good Game");

        assertThat(result.getNotes()).isEqualTo("Good Game");
        assertThat(result.getGameId()).isEqualTo(1L);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("updateSession: updates ended time")
    void updateSession_updatesStatus() {
        Session existing = buildSession(1L, "Good Game");

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(sessionRepository.save(existing)).thenReturn(existing);

        Session result = sessionService.updateSession(1L);

        assertThat(result.getEndedAt()).isNotNull();
        verify(sessionRepository, times(1)).save(any(Session.class));
    }


    @Test
    @DisplayName("gameExists: returns true when game-service responds")
    void gameExists_returnsTrueWhenClientResponds() {
        when(sessionClient.getResponse(1L)).thenReturn(new GameServiceResponse(
                1L, 1L, "Hades", "http://cover.jpg", 20, null, "PC", null
        ));

        assertThat(sessionService.gameExists(1L)).isTrue();
    }

    @Test
    @DisplayName("gameExists: returns false when game-service throws exception")
    void gameExists_returnsFalseWhenClientThrows() {
        when(sessionClient.getResponse(1L)).thenThrow(new RestClientException("connection refused"));

        assertThat(sessionService.gameExists(1L)).isFalse();
    }


    @Test
    @DisplayName("getAllSessions: returns all sessions from repository")
    void getAllSessions_returnsList() {
        when(sessionRepository.findAll()).thenReturn(List.of(buildSession(1L, "Good Game")));

        List<Session> result = sessionService.getAllSessions();

        assertThat(result).hasSize(1);
    }


    @Test
    @DisplayName("saveSession: throws SessionNotFoundException when not found")
    void saveSession_throwsWhenNotFound() {
        assertThatThrownBy(() -> sessionService.saveSession(99L, "Good Game"))
                .isInstanceOf(GameNotFoundException.class);
    }

    @Test
    @DisplayName("updateSession: throws SessionNotFoundException when not found")
    void updateSession_throwsWhenNotFound() {
        assertThatThrownBy(() -> sessionService.updateSession(99L))
                .isInstanceOf(SessionNotFoundException.class);
    }

    @Test
    @DisplayName("updateSession: throws SessionEndedException when session already ended")
    void updateSession_throwsWhenSessionAlreadyEnded() {
        Session ended = buildSession(1L, "Good Game");
        ended.setEndedAt(LocalDateTime.now());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(ended));

        assertThatThrownBy(() -> sessionService.updateSession(1L))
                .isInstanceOf(SessionEndedException.class);
    }
}