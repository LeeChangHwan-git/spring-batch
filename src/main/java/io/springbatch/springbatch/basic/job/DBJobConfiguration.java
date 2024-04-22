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
public class DBJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public DBJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean(name = "dbJob")
    public Job job() {
        return new JobBuilder("dbJob", jobRepository)
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean(name = "dbStep1")
    public Step step1() {
        return new StepBuilder("dbStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> DBJobConfiguration Step1 Start");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean(name = "dbStep2")
    public Step step2() {
        return new StepBuilder("dbStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> DBJobConfiguration Step1 Start");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

}
