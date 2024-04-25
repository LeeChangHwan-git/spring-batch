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
public class JobRepositoryConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobRepositoryConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobRepositoryJob() {
        return new JobBuilder("jobRepositoryJob", jobRepository)
                .start(jobRepositoryStep1())
                .next(jobRepositoryStep2())
//                .listener(jobExecutionListener)
                .build();
    }

    @Bean
    public Step jobRepositoryStep1() {
        return new StepBuilder("jobRepositoryStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobRepositoryStep1 start");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobRepositoryStep2() {
        return new StepBuilder("jobRepositoryStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobRepositoryStep2 start");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
