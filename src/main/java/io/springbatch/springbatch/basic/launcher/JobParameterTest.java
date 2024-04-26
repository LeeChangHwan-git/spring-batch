//package io.springbatch.springbatch.basic.launcher;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Map;
//
//@Component
//public class JobParameterTest implements ApplicationRunner {
//    private final JobLauncher jobLauncher;
//
//    // bean등록된 job을 가지고 있는 Map
//    private Map<String, Job> jobMap;
//
////    @Autowired
//    public JobParameterTest(JobLauncher jobLauncher, Map<String, Job> jobMap) {
//        this.jobLauncher = jobLauncher;
//        this.jobMap = jobMap;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("name", "user1")
//                .addLong("seq", 2L)
//                .addDate("date", new Date())
//                .addDouble("age", 16.5)
//                .toJobParameters();
//
//        String jobName = "jobRepositoryJob";
//        Job job = jobMap.get(jobName);
//
//        jobLauncher.run(job, jobParameters);
//    }
//}
