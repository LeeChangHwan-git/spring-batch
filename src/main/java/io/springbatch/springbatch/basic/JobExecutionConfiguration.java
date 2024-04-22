package io.springbatch.springbatch.basic;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobExecutionConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public JobExecutionConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobExecutionJob() {
        return new JobBuilder("jobExecutionJob", jobRepository)
                .start(jobExecutionStep1())
                .next(jobExecutionStep2())
                .build();
    }

    @Bean
    public Step jobExecutionStep1() {
        return new StepBuilder("jobExecutionStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobExecutionStep1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobExecutionStep2() {
        return new StepBuilder("jobExecutionStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobExecutionStep2 start!!");
//                    throw new RuntimeException(">>> jobExecutionStep2 failed");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
