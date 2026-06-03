package io.bandi.gamelens.batch.service;

import io.bandi.gamelens.batch.domain.model.Recommendation;
import io.bandi.gamelens.batch.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository repository;

    public RecommendationService(RecommendationRepository repository) {
        this.repository = repository;
    }


    public List<Recommendation> getAllRecommendations() {
        return repository.findAllByOrderByScoreDesc();
    }
}
