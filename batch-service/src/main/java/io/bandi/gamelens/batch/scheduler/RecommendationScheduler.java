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

/**
 * Triggers the recommendation job on a fixed schedule.
 *
 * <p>Currently configured to run every minute. Adjust the cron expression
 * in {@code application.yaml} for production use.
 */
@Component
public class RecommendationScheduler {

    private final Job recommendationJob;
    private final JobOperator jobOperator;

    public RecommendationScheduler(Job recommendationJob, JobOperator jobOperator) {
        this.recommendationJob = recommendationJob;
        this.jobOperator = jobOperator;
    }

    /**
     * Launches the recommendation job with a unique {@code runAt} parameter
     * so Spring Batch treats each execution as a new job instance.
     */
    @Scheduled(cron = "${recommendation.scheduler.cron}")
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
