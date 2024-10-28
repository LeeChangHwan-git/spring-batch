package io.springbatch.springbatch.basic.job.transition;

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
public class TransitionConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public TransitionConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job transitionJob() {
        return new JobBuilder("transitionJob", jobRepository)
                .start(transitionStep1())
                    .on("FAILED")
                    .to(transitionStep2())
                    .on("FAILED")
                    .stop()
                .from(transitionStep1())
                    .on("*")
                    .to(transitionStep3())
                    .next(transitionStep4())
                .from(transitionStep2())
                    .on("*")
                    .to(transitionStep5())
                .end()
                .build();
    }

    @Bean
    public Step transitionStep1() {
        return new StepBuilder("transitionStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("transitionStep1 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step transitionStep2() {
        return new StepBuilder("transitionStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("transitionStep2 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step transitionStep3() {
        return new StepBuilder("transitionStep3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("transitionStep3 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step transitionStep4() {
        return new StepBuilder("transitionStep4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("transitionStep4 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step transitionStep5() {
        return new StepBuilder("transitionStep5", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("transitionStep5 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step transitionStep6() {
        return new StepBuilder("transitionStep6", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("transitionStep6 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
