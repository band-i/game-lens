package io.bandi.gamelens.batch.job.step2;

import io.bandi.gamelens.batch.client.BatchGameClient;
import io.bandi.gamelens.batch.domain.dto.BacklogServiceResponse;
import io.bandi.gamelens.batch.domain.dto.GameServiceResponse;
import io.bandi.gamelens.batch.domain.model.Recommendation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationItemProcessorTest {

    @Mock
    private BatchGameClient batchGameClient;

    @InjectMocks
    private RecommendationItemProcessor processor;

    private GameServiceResponse buildGameResponse(double rating, int playtime) {
        return new GameServiceResponse(1L, 1L, "Hades", "http://cover.jpg",
                playtime, rating, "PC", null);
    }


    private BacklogServiceResponse buildBacklogResponse(int priority) {
        return new BacklogServiceResponse(1L, 1L, "PENDING", priority, null, null);
    }

    @Test
    @DisplayName("process: calculates score correctly")
    void process_calculatesScoreCorrectly() throws Exception {
        when(batchGameClient.getResponse(1L)).thenReturn(buildGameResponse(4.5, 20));

        Recommendation result = processor.process(buildBacklogResponse(3));

        int expected = (int) ((4.5 * 10) + (3 * 5) - (20 * 0.1));
        assertThat(result.getScore()).isEqualTo(expected);
    }

    @Test
    @DisplayName("process: adds high community rating reason when rating above 4")
    void process_addsHighRatingReason() throws Exception {
        when(batchGameClient.getResponse(1L)).thenReturn(buildGameResponse(4.5, 20));

        Recommendation result = processor.process(buildBacklogResponse(1));

        assertThat(result.getReason()).contains("high community rating");
    }

    @Test
    @DisplayName("process: adds priority reason when priority above 4")
    void process_addsPriorityReason() throws Exception {
        when(batchGameClient.getResponse(1L)).thenReturn(buildGameResponse(3.0, 20));

        Recommendation result = processor.process(buildBacklogResponse(5));

        assertThat(result.getReason()).contains("your priority");
    }

    @Test
    @DisplayName("process: adds short playtime reason when playtime below 10")
    void process_addsShortPlaytimeReason() throws Exception {
        when(batchGameClient.getResponse(1L)).thenReturn(buildGameResponse(3.0, 5));

        Recommendation result = processor.process(buildBacklogResponse(1));

        assertThat(result.getReason()).contains("its short playtime");
    }

    @Test
    @DisplayName("process: uses default reason when no conditions are met")
    void process_usesDefaultReasonWhenNoConditionsMet() throws Exception {
        when(batchGameClient.getResponse(1L)).thenReturn(buildGameResponse(3.0, 20));

        Recommendation result = processor.process(buildBacklogResponse(1));

        assertThat(result.getReason()).isEqualTo("Recommended based on overall score");
    }

    @Test
    @DisplayName("process: combines multiple reasons")
    void process_combinesMultipleReasons() throws Exception {
        when(batchGameClient.getResponse(1L)).thenReturn(buildGameResponse(4.5, 5));

        Recommendation result = processor.process(buildBacklogResponse(5));

        assertThat(result.getReason()).contains("high community rating");
        assertThat(result.getReason()).contains("your priority");
        assertThat(result.getReason()).contains("its short playtime");
    }

}