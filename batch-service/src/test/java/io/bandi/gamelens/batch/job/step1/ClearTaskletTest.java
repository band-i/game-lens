package io.bandi.gamelens.batch.job.step1;

import io.bandi.gamelens.batch.repository.RecommendationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClearTaskletTest {

    @Mock
    private RecommendationRepository repository;

    @Mock
    private StepContribution stepContribution;

    @Mock
    private ChunkContext chunkContext;

    @InjectMocks
    private ClearTasklet clearTasklet;

    @Test
    @DisplayName("execute: deletes all recommendations and returns FINISHED")
    void execute_deletesAllAndReturnsFinished() {
        RepeatStatus result = clearTasklet.execute(stepContribution, chunkContext);

        verify(repository, times(1)).deleteAll();
        assertThat(result).isEqualTo(RepeatStatus.FINISHED);
    }
}