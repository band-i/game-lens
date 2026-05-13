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

@Service
public class BacklogService {

    private final BacklogClient backlogClient;
    private final BacklogRepository backlogRepository;

    public BacklogService(BacklogClient backlogClient, BacklogRepository backlogRepository) {
        this.backlogClient = backlogClient;
        this.backlogRepository = backlogRepository;
    }

    public Backlog saveBacklog(Long gameId, Integer priority) {
        Backlog backlog = new Backlog();
        backlog.setGameId(gameId);
        backlog.setStatus(Status.PENDING);
        backlog.setPriority(priority);
        backlog.setCreatedAt(LocalDateTime.now());
        backlog.setUpdatedAt(LocalDateTime.now());

        return backlogRepository.save(backlog);
    }

    public Backlog updateBacklog(Long gameId, BacklogRequest backlogRequest) {
        Backlog backlog = getBacklogById(gameId);
        backlog.setStatus(backlogRequest.status());

        return backlogRepository.save(backlog);
    }

    public void deleteBacklog(Long id) {
        backlogRepository.deleteById(id);
    }

    public boolean gameExists(Long gameId) {
        try {
            return backlogClient.getResponse(gameId) != null;
        } catch (RestClientException e) {
            return false;
        }
    }

    public boolean backlogExists(Long gameId) {
        return backlogRepository.existsByGameId(gameId);
    }

    public Backlog getBacklogById(Long gameId) {
        return backlogRepository.findByGameId(gameId);
    }

    public List<Backlog> getAllBacklogs() {
        return backlogRepository.findAll();
    }
}
