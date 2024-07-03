package io.springbatch.springbatch.basic.step;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TaskletStepConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public TaskletStepConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job taskletStepJob() {
        return new JobBuilder("taskletStepJob", jobRepository)
                .start(taskletStep1())
                .build();
    }

//    @Bean
//    public Step taskletStep1() {
//        return new StepBuilder("taskletStep1", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//                    System.out.println(">>taskletStep1 was executed!!!");
//                    return RepeatStatus.FINISHED;
//                }, transactionManager)
//                .build();
//    }

    @Bean
    public Step taskletStep1() {
        return new StepBuilder("taskletStep1", jobRepository)
                .tasklet(new CustomTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step chunkStep1() {
        return new StepBuilder("chunkStep1", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3")))
                .processor(String::toUpperCase)
                .writer(chunk -> chunk.getItems().forEach(System.out::println))
                .build();
    }
}
