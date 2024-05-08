package io.springbatch.springbatch.basic.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobBuilderConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobBuilderConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobBuilderJob1() {
        return new JobBuilder("jobBuilderJob", jobRepository)
                .start(jobBuilderStep1())
                .build();

    }

    @Bean
    public Job jobBuilderJob2() {
        return new JobBuilder("jobBuildJob2", jobRepository)
                .start(jobBuilderFlow())
                .next(jobBuilderStep4())
                .end()
                .build();
    }

    @Bean
    public Flow jobBuilderFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("jobBuilderFlow");
        flowBuilder.start(jobBuilderStep2())
                .next(jobBuilderStep3())
                .end();
        return flowBuilder.build();
    }

    @Bean
    public Step jobBuilderStep1() {
        return new StepBuilder("jobBuilderStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobBuilderStep1 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobBuilderStep2() {
        return new StepBuilder("jobBuilderStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobBuilderStep2 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobBuilderStep3() {
        return new StepBuilder("jobBuilderStep3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobBuilderStep3 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobBuilderStep4() {
        return new StepBuilder("jobBuilderStep4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobBuilderStep4 start!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
