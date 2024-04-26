package io.springbatch.springbatch.basic.step;

import io.springbatch.springbatch.basic.tasklet.ErrorTasklet;
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
public class StepExecutionConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public StepExecutionConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job stepExecutionJob() {
        return new JobBuilder("stepExecutionJob", jobRepository)
                .start(stepExecutionStep1())
                .next(stepExecutionStep2())
                .build();
    }

    @Bean
    public Step stepExecutionStep1() {
        return new StepBuilder("stepExecutionStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> stepExecutionStep1 start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step stepExecutionStep2() {
        return new StepBuilder("stepExecutionStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> stepExecutionStep2 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();

//        return new StepBuilder("stepExecutionStep2", jobRepository)
//                .tasklet(new ErrorTasklet(), transactionManager)
//                .build();
    }
}
