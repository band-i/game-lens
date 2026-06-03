package io.bandi.gamelens.batch.scheduler;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RecommendationScheduler {

    private final Job recommendationJob;
    private final JobOperator jobOperator;

    public RecommendationScheduler(Job recommendationJob, JobOperator jobOperator) {
        this.recommendationJob = recommendationJob;
        this.jobOperator = jobOperator;
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void run() throws JobInstanceAlreadyCompleteException,
            InvalidJobParametersException,
            JobExecutionAlreadyRunningException,
            JobRestartException {
        jobOperator.start(recommendationJob, new JobParametersBuilder()
                .addLocalDateTime("runAt", LocalDateTime.now())
                .toJobParameters()
        );
    }
}
