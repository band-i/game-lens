package io.bandi.gamelens.batch.job.step2;

import io.bandi.gamelens.batch.client.BatchGameClient;
import io.bandi.gamelens.batch.domain.dto.BacklogServiceResponse;
import io.bandi.gamelens.batch.domain.dto.GameServiceResponse;
import io.bandi.gamelens.batch.domain.model.Recommendation;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecommendationItemProcessor implements ItemProcessor<BacklogServiceResponse, Recommendation> {

    private final BatchGameClient batchGameClient;

    public RecommendationItemProcessor(BatchGameClient batchGameClient) {
        this.batchGameClient = batchGameClient;
    }

    @Override
    public @Nullable Recommendation process(BacklogServiceResponse item) throws Exception {
        Recommendation recommendation = new Recommendation();
        GameServiceResponse response = batchGameClient.getResponse(item.gameId());

        Double rating = response.rating() * 10;
        Integer priority = item.priority() != null ? item.priority() * 5 : 0;
        Double averagePlaytime = response.averagePlaytime() * 0.1;

        recommendation.setGameId(response.id());
        recommendation.setScore((int) (rating + priority - averagePlaytime));

        List<String> reasons = new ArrayList<>();

        if (response.rating() > 4) {
            reasons.add("high community rating");
        }
        if (item.priority() > 4) {
            reasons.add("your priority");
        }
        if (response.averagePlaytime() < 10) {
            reasons.add("its short playtime");
        }

        if (reasons.isEmpty()) {
            recommendation.setReason("Recommended based on overall score");
        } else {
            recommendation.setReason("Recommended based on " + String.join(" and ", reasons));
        }
        return recommendation;
    }
}
