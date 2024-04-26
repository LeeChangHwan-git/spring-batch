package io.springbatch.springbatch.basic.job;

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
public class JobLauncherConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public JobLauncherConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job jobLauncherJob() {
        return new JobBuilder("jobLauncherJob", jobRepository)
                .start(jobLauncherStep1())
                .next(jobLauncherStep2())
                .build();
    }

    @Bean
    public Step jobLauncherStep1() {
        return new StepBuilder("jobLauncherStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobLuancherStep1 start!");
                    // 비동기 테스트를 눈으로 확인하기 위해 Thread.sleep 걸어준다
                    // 동기실행을 하면 수행시간이 5초이상 걸림
                    // 비동기실행은 바로 200을 주지만 실제로 작업은 5초이상 걸린다.
                    Thread.sleep(5000);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step jobLauncherStep2() {
        return new StepBuilder("jobLauncherStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(">>> jobLuancherStep2 start!");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
