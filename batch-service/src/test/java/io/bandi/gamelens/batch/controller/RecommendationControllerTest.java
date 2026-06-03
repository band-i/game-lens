package io.bandi.gamelens.batch.controller;

import io.bandi.gamelens.batch.domain.model.Recommendation;
import io.bandi.gamelens.batch.service.RecommendationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;


    private Recommendation buildRecommendation(Long gameId, Integer score, String reason) {
        Recommendation recommendation = new Recommendation();
        recommendation.setGameId(gameId);
        recommendation.setScore(score);
        recommendation.setReason(reason);
        return recommendation;
    }

    @Test
    @DisplayName("getAllRecommendations: returns 200 with list")
    void getAllRecommendations_returns200WithList() throws Exception {
        when(recommendationService.getAllRecommendations()).thenReturn(List.of(buildRecommendation(1L, 4, "Recommended based on overall score")));

        mockMvc.perform(get("/api/v1/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}