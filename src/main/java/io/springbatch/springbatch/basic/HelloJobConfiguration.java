package io.springbatch.springbatch.basic;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class HelloJobConfiguration {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public HelloJobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean(name = "helloJob")
    public Job helloJob(Step helloStep1, Step helloStep2) {
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep1)
                .next(helloStep2)
                .build();
    }

    @Bean
    public Step helloStep1() {
        return new StepBuilder("helloStep1",jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("==========================");
                    System.out.println(">> Hello Spring Batch!!");
                    System.out.println("==========================");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
    @Bean
    public Step helloStep2() {
        return new StepBuilder("helloStep2",jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    System.out.println("==========================");
                    System.out.println(">> Step2 Started");
                    System.out.println("==========================");

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
