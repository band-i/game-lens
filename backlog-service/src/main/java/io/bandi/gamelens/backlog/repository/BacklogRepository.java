package io.bandi.gamelens.backlog.repository;

import io.bandi.gamelens.backlog.domain.model.Backlog;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacklogRepository extends ListCrudRepository<Backlog, Long> {
    Boolean existsByGameId(Long gameId);

    Backlog findByGameId(Long gameId);
}
