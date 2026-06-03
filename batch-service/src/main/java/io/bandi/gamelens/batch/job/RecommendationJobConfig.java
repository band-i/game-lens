package io.bandi.gamelens.batch.job;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendationJobConfig {

    private final JobRepository jobRepository;

    public RecommendationJobConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job recommendationJob(
            TaskletStep clearTaskletStep,
            Step recommendationStep
    ) {
        return new JobBuilder("recommendationJob", jobRepository)
                .start(clearTaskletStep)
                .next(recommendationStep)
                .build();
    }
}
