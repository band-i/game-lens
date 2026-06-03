package io.bandi.gamelens.batch.job.step2;

import io.bandi.gamelens.batch.client.BatchBacklogClient;
import io.bandi.gamelens.batch.domain.dto.BacklogServiceResponse;
import io.bandi.gamelens.batch.domain.model.Recommendation;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RecommendationStepConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ItemReader<BacklogServiceResponse> batchItemReader;
    private final ItemProcessor<BacklogServiceResponse, Recommendation> batchItemProcessor;
    private final ItemWriter<Recommendation> batchItemWriter;

    public RecommendationStepConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<BacklogServiceResponse> batchItemReader,
            ItemProcessor<BacklogServiceResponse, Recommendation> batchItemProcessor,
            ItemWriter<Recommendation> batchItemWriter
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.batchItemReader = batchItemReader;
        this.batchItemProcessor = batchItemProcessor;
        this.batchItemWriter = batchItemWriter;
    }

    @Bean
    @StepScope
    public RecommendationItemReader batchItemReader(BatchBacklogClient batchBacklogClient) {
        return new RecommendationItemReader(batchBacklogClient);
    }

    @Bean
    public Step recommendationStep() {
        return new StepBuilder("stepBuilder", jobRepository)
                .<BacklogServiceResponse, Recommendation>chunk(10)
                .reader(batchItemReader)
                .processor(batchItemProcessor)
                .writer(batchItemWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
