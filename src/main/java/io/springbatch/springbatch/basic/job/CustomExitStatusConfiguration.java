package io.springbatch.springbatch.basic.job;

import org.springframework.batch.core.ExitStatus;
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
public class CustomExitStatusConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public CustomExitStatusConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job customExitStatusJob() {
        return new JobBuilder("customExitStatusJob", jobRepository)
                .start(customExitStatusStep1())
                .on("FAILED")
                .to(customExitStatusStep2())
                .on("PASS")
                .stop()
                .end()
                .build();
    }

    @Bean
    public Step customExitStatusStep1() {
        return new StepBuilder("customExitStatusStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Custom Exit Status Step1 Executed!!");
                    contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);
//                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step customExitStatusStep2() {
        return new StepBuilder("customExitStatusStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Custom Exit Status Step2 Executed!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .listener(new PassCheckingListener())
                .build();
    }
}
