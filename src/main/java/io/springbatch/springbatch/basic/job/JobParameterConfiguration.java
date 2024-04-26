package io.springbatch.springbatch.basic.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class JobParameterConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobParameterConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean(name = "jobParameterJob")
    public Job jobParameterJob() {
        return new JobBuilder("jobParameterJob", jobRepository)
                .start(jobParameterStep1())
                .next(jobParameterStep2())
                .build();
    }

    public Step jobParameterStep1() {
        return new StepBuilder("jobParameterStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {

                    JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                    jobParameters.getString("name");
                    jobParameters.getLong("seq");
                    jobParameters.getDate("date");
                    jobParameters.getDouble("age");

                    Map<String, Object> jobParameters2 = chunkContext.getStepContext().getJobParameters();



                    System.out.println(">>> jobParameterStep1 Start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    public Step jobParameterStep2() {
        return new StepBuilder("jobParameterStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobParameterStep2 Start!!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
