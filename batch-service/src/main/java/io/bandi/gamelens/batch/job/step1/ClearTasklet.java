package io.bandi.gamelens.batch.job.step1;

import io.bandi.gamelens.batch.repository.RecommendationRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ClearTasklet implements Tasklet {

    private final RecommendationRepository repository;

    public ClearTasklet(RecommendationRepository repository) {
        this.repository = repository;
    }

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        repository.deleteAll();
        return RepeatStatus.FINISHED;
    }
}
