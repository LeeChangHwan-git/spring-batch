package io.springbatch.springbatch.basic.job.flow;

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
public class FlowJobConfiguration2 {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public FlowJobConfiguration2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job flowJob2() {
        return new JobBuilder("flowJob2", jobRepository)
                .start(flowA())
                .next(flowJobStep3())
                .next(flowB())
                .next(flowJobStep6())
                .end()
                .build();
    }

    @Bean
    public Flow flowA() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowA");
        flowBuilder.start(flowJobStep1())
                .next(flowJobStep2())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Flow flowB() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowB");
        flowBuilder.start(flowJobStep4())
                .next(flowJobStep5())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Step flowJobStep1() {
        return new StepBuilder("flowJobStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowJobStep2() {
        return new StepBuilder("flowJobStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowJobStep3() {
        return new StepBuilder("flowJobStep3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step3 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowJobStep4() {
        return new StepBuilder("flowJobStep4", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step4 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowJobStep5() {
        return new StepBuilder("flowJobStep5", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step5 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step flowJobStep6() {
        return new StepBuilder("flowJobStep6", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step6 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
