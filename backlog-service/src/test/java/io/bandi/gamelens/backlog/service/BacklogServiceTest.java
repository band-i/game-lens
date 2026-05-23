package io.bandi.gamelens.backlog.service;

import io.bandi.gamelens.backlog.client.BacklogClient;
import io.bandi.gamelens.backlog.domain.dto.BacklogRequest;
import io.bandi.gamelens.backlog.domain.dto.GameServiceResponse;
import io.bandi.gamelens.backlog.domain.model.Backlog;
import io.bandi.gamelens.backlog.domain.model.Status;
import io.bandi.gamelens.backlog.repository.BacklogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BacklogServiceTest {

    @Mock
    private BacklogClient backlogClient;

    @Mock
    private BacklogRepository backlogRepository;

    @InjectMocks
    private BacklogService backlogService;

    private Backlog buildBacklog(Long gameId, Status status) {
        Backlog backlog = new Backlog();
        backlog.setGameId(gameId);
        backlog.setStatus(status);
        backlog.setPriority(1);
        return backlog;
    }

    // --- saveBacklog ---

    @Test
    @DisplayName("saveBacklog: persists backlog with PENDING status")
    void saveBacklog_persistsWithPendingStatus() {
        Backlog saved = buildBacklog(1L, Status.PENDING);
        when(backlogRepository.save(any(Backlog.class))).thenReturn(saved);

        Backlog result = backlogService.saveBacklog(1L, 1);

        assertThat(result.getStatus()).isEqualTo(Status.PENDING);
        assertThat(result.getGameId()).isEqualTo(1L);
        verify(backlogRepository, times(1)).save(any(Backlog.class));
    }

    // --- updateBacklog ---

    @Test
    @DisplayName("updateBacklog: updates status and persists")
    void updateBacklog_updatesStatus() {
        Backlog existing = buildBacklog(1L, Status.PENDING);
        BacklogRequest request = new BacklogRequest(Status.IN_PROGRESS);
        Backlog updated = buildBacklog(1L, Status.IN_PROGRESS);

        when(backlogRepository.findByGameId(1L)).thenReturn(existing);
        when(backlogRepository.save(any(Backlog.class))).thenReturn(updated);

        Backlog result = backlogService.updateBacklog(1L, request);

        assertThat(result.getStatus()).isEqualTo(Status.IN_PROGRESS);
        verify(backlogRepository, times(1)).save(any(Backlog.class));
    }

    // --- deleteBacklog ---

    @Test
    @DisplayName("deleteBacklog: delegates to repository")
    void deleteBacklog_delegatesToRepository() {
        backlogService.deleteBacklog(1L);

        verify(backlogRepository, times(1)).deleteById(1L);
    }

    // --- gameExists ---

    @Test
    @DisplayName("gameExists: returns true when game-service responds")
    void gameExists_returnsTrueWhenClientResponds() {
        when(backlogClient.getResponse(1L)).thenReturn(new GameServiceResponse(
                1L, 1L, "Hades", "http://cover.jpg", 20, null, "PC", null
        ));

        assertThat(backlogService.gameExists(1L)).isTrue();
    }

    @Test
    @DisplayName("gameExists: returns false when game-service throws exception")
    void gameExists_returnsFalseWhenClientThrows() {
        when(backlogClient.getResponse(1L)).thenThrow(new RestClientException("connection refused"));

        assertThat(backlogService.gameExists(1L)).isFalse();
    }

    // --- backlogExists ---

    @Test
    @DisplayName("backlogExists: delegates to repository")
    void backlogExists_delegatesToRepository() {
        when(backlogRepository.existsByGameId(1L)).thenReturn(true);

        assertThat(backlogService.backlogExists(1L)).isTrue();
        verify(backlogRepository).existsByGameId(1L);
    }

    // --- getBacklogById ---

    @Test
    @DisplayName("getBacklogById: returns backlog from repository")
    void getBacklogById_returnsBacklog() {
        Backlog backlog = buildBacklog(1L, Status.PENDING);
        when(backlogRepository.findByGameId(1L)).thenReturn(backlog);

        Backlog result = backlogService.getBacklogById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getGameId()).isEqualTo(1L);
    }

    // --- getAllBacklogs ---

    @Test
    @DisplayName("getAllBacklogs: returns all backlogs from repository")
    void getAllBacklogs_returnsList() {
        when(backlogRepository.findAll()).thenReturn(List.of(buildBacklog(1L, Status.PENDING)));

        List<Backlog> result = backlogService.getAllBacklogs();

        assertThat(result).hasSize(1);
    }

}