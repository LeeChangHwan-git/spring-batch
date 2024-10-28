package io.springbatch.springbatch.basic.job.flow;

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
public class FlowJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public FlowJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job flowJob() {
        return new JobBuilder("flowJob", jobRepository)
                .start(step1())
                .on("COMPLETED").to(step3())
                .from(step1())
                .on("FAILED").to(step2())
                .end()
                .build();
    }
    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("##################");
                    System.out.println("step1 started!");
                    System.out.println("##################");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("##################");
                    System.out.println("step2 started!");
                    System.out.println("##################");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step step3() {
        return new StepBuilder("step3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("##################");
                    System.out.println("step3 started!");
                    System.out.println("##################");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
