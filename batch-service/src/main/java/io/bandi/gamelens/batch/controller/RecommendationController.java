package io.bandi.gamelens.batch.controller;

import io.bandi.gamelens.batch.domain.model.Recommendation;
import io.bandi.gamelens.batch.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Recommendation", description = "Recommendation management")
@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }


    /**
     * Returns all recommendations from the local.
     */
    @Operation(summary = "Get all recommendation entries", description = "Returns all entries in the user's recommendations.")
    @ApiResponse(responseCode = "200", description = "List of recommendation entries, empty if none exist yet")
    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {
        return new ResponseEntity<>(recommendationService.getAllRecommendations(), HttpStatus.OK);
    }
}
