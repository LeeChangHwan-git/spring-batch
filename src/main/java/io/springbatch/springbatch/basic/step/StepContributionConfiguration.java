package io.springbatch.springbatch.basic.step;

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
public class StepContributionConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public StepContributionConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job stepContributionJob() {
        return new JobBuilder("stepContributionJob", jobRepository)
                .start(stepContributionStep1())
                .next(stepContributionStep2())
                .build();
    }

    @Bean
    public Step stepContributionStep1() {
        return new StepBuilder("stepContributionStep1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>> stepContributionStep start!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }

    @Bean
    public Step stepContributionStep2() {
        return new StepBuilder("stepContributionStep2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println(">>> stepContributionStep start!!");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
