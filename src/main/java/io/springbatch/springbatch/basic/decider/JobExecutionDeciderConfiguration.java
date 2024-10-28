package io.springbatch.springbatch.basic.decider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobExecutionDeciderConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobExecutionDeciderConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobExecutionDeciderJob() {
        return new JobBuilder("jobExecutionDeciderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(jobExecutionDeciderStep1())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .end()
                .build();
    }

    private JobExecutionDecider decider() {
        return new CustomDecider();
    }

    @Bean
    public Step jobExecutionDeciderStep1() {
        return new StepBuilder("jobExecutionDeciderStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("JobExecutionDeciderStep1 executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step evenStep() {
        return new StepBuilder("evenStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("evenStep executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step oddStep() {
        return new StepBuilder("oddStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("oddStep executed!!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
