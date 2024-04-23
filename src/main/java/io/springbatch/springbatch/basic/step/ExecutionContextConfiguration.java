package io.springbatch.springbatch.basic.step;

import io.springbatch.springbatch.basic.tasklet.ExecutionContextTasklet1;
import io.springbatch.springbatch.basic.tasklet.ExecutionContextTasklet2;
import io.springbatch.springbatch.basic.tasklet.ExecutionContextTasklet3;
import io.springbatch.springbatch.basic.tasklet.ExecutionContextTasklet4;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ExecutionContextConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ExecutionContextTasklet1 executionContextTasklet1;
    private final ExecutionContextTasklet2 executionContextTasklet2;
    private final ExecutionContextTasklet3 executionContextTasklet3;
    private final ExecutionContextTasklet4 executionContextTasklet4;

    public ExecutionContextConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                         ExecutionContextTasklet1 executionContextTasklet1,
                                         ExecutionContextTasklet2 executionContextTasklet2,
                                         ExecutionContextTasklet3 executionContextTasklet3,
                                         ExecutionContextTasklet4 executionContextTasklet4) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.executionContextTasklet1 = executionContextTasklet1;
        this.executionContextTasklet2 = executionContextTasklet2;
        this.executionContextTasklet3 = executionContextTasklet3;
        this.executionContextTasklet4 = executionContextTasklet4;
    }

    @Bean
    public Job executionContextJob() {
        return new JobBuilder("executionContextJob", jobRepository)
                .start(executionContextStep1())
                .next(executionContextStep2())
                .next(executionContextStep3())
                .next(executionContextStep4())
                .build();
    }

    @Bean
    public Step executionContextStep1() {
      return new StepBuilder("executionContextStep1", jobRepository)
              .tasklet(executionContextTasklet1, transactionManager)
              .build();
    }

    @Bean
    public Step executionContextStep2() {
        return new StepBuilder("executionContextStep2", jobRepository)
                .tasklet(executionContextTasklet2, transactionManager)
                .build();
    }

    @Bean
    public Step executionContextStep3() {
        return new StepBuilder("executionContextStep3", jobRepository)
                .tasklet(executionContextTasklet3, transactionManager)
                .build();
    }

    @Bean
    public Step executionContextStep4() {
        return new StepBuilder("executionContextStep4", jobRepository)
                .tasklet(executionContextTasklet4, transactionManager)
                .build();
    }
}
