package io.bandi.gamelens.batch.job.step1;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ClearTaskletConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ClearTasklet clearTasklet;

    public ClearTaskletConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ClearTasklet clearTasklet
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.clearTasklet = clearTasklet;
    }


    @Bean
    public TaskletStep clearTaskletStep() {
        return new StepBuilder("clearTaskletStep", jobRepository)
                .tasklet(clearTasklet)
                .transactionManager(transactionManager)
                .build();
    }
}
