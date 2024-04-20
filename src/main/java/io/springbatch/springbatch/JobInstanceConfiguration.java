package io.springbatch.springbatch;

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
public class JobInstanceConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobInstanceConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

//    @Bean(name = "jobInstanceJob")
    @Bean(name = "jobInstanceJob")
    public Job jobInstanceJob() {
        return new JobBuilder("jobInstanceJob", jobRepository)
                .start(jobInstanceStep1())
                .next(jobInstanceStep2())
                .build();
    }

    @Bean
    public Step jobInstanceStep1() {
        return new StepBuilder("jobInstanceStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> jobInstanceStep1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
    @Bean
    public Step jobInstanceStep2() {
        return new StepBuilder("jobInstanceStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">> jobInstanceStep2 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
