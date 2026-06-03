package io.bandi.gamelens.batch.service;

import io.bandi.gamelens.batch.domain.model.Recommendation;
import io.bandi.gamelens.batch.repository.RecommendationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @InjectMocks
    private RecommendationService recommendationService;

    private Recommendation buildRecommendation(Long gameId, Integer score, String reason) {
        Recommendation recommendation = new Recommendation();
        recommendation.setGameId(gameId);
        recommendation.setScore(score);
        recommendation.setReason(reason);
        return recommendation;
    }


    @Test
    @DisplayName("getAllRecommendations: returns all recommendations from repository")
    void getAllRecommendations_returnsList() {
        when(recommendationRepository.findAllByOrderByScoreDesc()).thenReturn(List.of(buildRecommendation(1L, 4, "Recommended based on overall score")));

        List<Recommendation> result = recommendationService.getAllRecommendations();

        assertThat(result).hasSize(1);
    }
}