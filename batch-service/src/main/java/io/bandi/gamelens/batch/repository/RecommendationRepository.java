package io.bandi.gamelens.batch.repository;

import io.bandi.gamelens.batch.domain.model.Recommendation;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends ListCrudRepository<Recommendation, Long> {
    List<Recommendation> findAllByOrderByScoreDesc();
}
