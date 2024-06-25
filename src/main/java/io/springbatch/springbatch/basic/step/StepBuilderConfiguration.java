package io.springbatch.springbatch.basic.step;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepBuilderConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public StepBuilderConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job stepBuilderJob() {
        return new JobBuilder("stepBuidlerJob", jobRepository)
                .start(stepBuilderStep1())
                .next(stepBuilderStep2())
                .next(stepBuilderStep3())
                .build();
    }

    @Bean
    public Step stepBuilderStep1() {
        return new StepBuilder("stepBuilderStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
                .build();
    }

    @Bean
    public Step stepBuilderStep2() {
        return new StepBuilder("stepBuilderStep2", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(new ItemReader<String>() {
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return null;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(Chunk<? extends String> items) throws Exception {

                    }
                })
                .build();
    }

    @Bean
    public Step stepBuilderStep3() {
        return new StepBuilder("stepBuilderStep3", jobRepository)
                .partitioner(stepBuilderStep1())
                .gridSize(2)
                .build();
    }

    @Bean
    public Step stepBuilderStep4() {
        return new StepBuilder("stepBuilderStep4", jobRepository)
                .job(job())
                .build();
    }

    @Bean
    public Step stepBuilderStep5() {
        return new StepBuilder("stepBuilderStep5", jobRepository)
                .flow(flow())
                .build();
    }

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
                .start(stepBuilderStep1())
                .next(stepBuilderStep2())
                .next(stepBuilderStep3())
                .build();
    }

    @Bean
    public Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder.start(stepBuilderStep2()).end();
        return flowBuilder.build();
    }
}
