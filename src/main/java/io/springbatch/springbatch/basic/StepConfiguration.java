package io.springbatch.springbatch.basic;

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
public class StepConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public StepConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job stepJob() {
        return new JobBuilder("stepJob", jobRepository)
                .start(stepJobStep1())
                .next(stepJobStep2())
                .build();
    }

    @Bean
    public Step stepJobStep1() {
        return new StepBuilder("stepJobStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> stepJobStep1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step stepJobStep2() {
        return new StepBuilder("stepJobStep2", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }
}
