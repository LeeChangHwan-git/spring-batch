//package io.springbatch.springbatch.basic.step;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//public class JobStepConfiguration {
//    private final JobRepository jobRepository;
//    private final PlatformTransactionManager transactionManager;
//
//    public JobStepConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//        this.jobRepository = jobRepository;
//        this.transactionManager = transactionManager;
//    }
//
//    @Bean
//    public Job parentJob() {
//        return new JobBuilder("parentJob", jobRepository)
//                .start(jobStep(null))
//                .next(step2())
//                .build();
//    }
//
//    @Bean
//    public Step jobStep(@Autowired JobLauncher jobLauncher) {
//        return new StepBuilder("jobStep", jobRepository)
//                .job(childJob())
//                .launcher(jobLauncher)
//                .parametersExtractor(jobParametersExtractor())
//                .build();
//    }
//
//    private DefaultJobParametersExtractor jobParametersExtractor() {
//        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
//        extractor.setKeys(new String[]{"name"});
//        return extractor;
//    }
//
//    @Bean
//    public Job childJob() {
//        return new JobBuilder("childJob", jobRepository)
//                .start(step1())
//                .build();
//    }
//
////    @Bean
//    public Step step1() {
//        return new StepBuilder("step1", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//                    System.out.println("step1 start!");
//                    return RepeatStatus.FINISHED;
//                }, transactionManager)
//                .build();
//    }
//
////    @Bean
//    public Step step2() {
//        return new StepBuilder("step2", jobRepository)
//                .tasklet((contribution, chunkContext) -> {
//                    System.out.println("step2 start!");
//                    return RepeatStatus.FINISHED;
//                }, transactionManager)
//                .build();
//    }
//}
