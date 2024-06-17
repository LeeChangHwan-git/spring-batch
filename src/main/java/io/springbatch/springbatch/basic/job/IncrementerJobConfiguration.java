package io.springbatch.springbatch.basic.job;

import io.springbatch.springbatch.basic.incrementer.CustomJobParametersIncrementer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class IncrementerJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public IncrementerJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job incrementerJob() {
        return new JobBuilder("incrementerJob", jobRepository)
                .start(incrementerJobStep1())
                .next(incrementerJobStep2())
                .incrementer(new CustomJobParametersIncrementer())
                .build();
    }

    @Bean
    public Step incrementerJobStep1() {
        return new StepBuilder("incrementerJobStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> incrementerJobStep1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step incrementerJobStep2() {
        return new StepBuilder("incrementerJobStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> incrementerJobStep2 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
