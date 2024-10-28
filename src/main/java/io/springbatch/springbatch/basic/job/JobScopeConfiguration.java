package io.springbatch.springbatch.basic.job;

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
public class JobScopeConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobScopeConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobScopeJob() {
        return new JobBuilder("jobScopeJob", jobRepository)
                .start(jobScopeStep1())
                .next(jobScopeStep2())
                .build();
    }

    @Bean
    public Step jobScopeStep1() {
        return new StepBuilder("jobScopeStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobScopeStep2() {
        return new StepBuilder("jobScopeStep1", jobRepository)
                .tasklet(tasklet1(), transactionManager)
                .build();
    }

    private Tasklet tasklet1() {
        return null;
    }
}
