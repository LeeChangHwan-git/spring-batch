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
public class JobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobConfigurationJob() {
        return new JobBuilder("jobConfigurationJob", jobRepository)
                .start(jobConfigurationStep1())
                .next(jobConfigurationStep2())
                .build();
    }

    @Bean
    public Step jobConfigurationStep1() {
        return new StepBuilder("jobConfigurationStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> jobConfigurationStep1 Start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobConfigurationStep2() {
        return new StepBuilder("jobConfigurationStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> jobConfigurationStep2 Start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
