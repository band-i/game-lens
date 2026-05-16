package io.bandi.gamelens.game.repository;

import io.bandi.gamelens.game.domain.model.Game;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends ListCrudRepository<Game, Long> {
    Boolean existsByRawgId(Long rawgId);

    Game findByRawgId(Long rawgId);
}
