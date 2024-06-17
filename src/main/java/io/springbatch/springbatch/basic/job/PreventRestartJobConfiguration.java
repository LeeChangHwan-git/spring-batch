package io.springbatch.springbatch.basic.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PreventRestartJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public PreventRestartJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job preventRestartJob() {
        return new JobBuilder("preventRestartJob", jobRepository)
                .start(preventRestartJobStep1())
                .next(preventRestartJobStep2())
                .preventRestart()
                .build();
    }

    @Bean
    public Step preventRestartJobStep1() {
        return new StepBuilder("preventRestartJobStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> preventRestartJobStep1 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
    @Bean
    public Step preventRestartJobStep2() {
        return new StepBuilder("preventRestartJobStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> preventRestartJobStep2 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
