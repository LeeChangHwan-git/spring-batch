package io.springbatch.springbatch.basic.job;

import io.springbatch.springbatch.basic.validator.CustomParametersValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ValidatorJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public ValidatorJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    // CustomParametersValidator 사용
//    @Bean
//    public Job validatorJob() {
//        return new JobBuilder("validatorJob", jobRepository)
//                .start(validatorJobStep1())
//                .next(validatorJobStep2())
//                .validator(new CustomParametersValidator())
//                .build();
//    }

    // DefaultJobParametersValidator 사용
    @Bean
    public Job validatorJob() {
        return new JobBuilder("validatorJob", jobRepository)
                .start(validatorJobStep1())
                .next(validatorJobStep2())
                .validator(new DefaultJobParametersValidator(new String[]{"name", "date"}, new String[]{"count"}))
                .build();
    }

    @Bean
    public Step validatorJobStep1() {
        return new StepBuilder("validatorJobStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> validatorJobStep1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step validatorJobStep2() {
        return new StepBuilder("validatorJobStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> validatorJobStep2 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
