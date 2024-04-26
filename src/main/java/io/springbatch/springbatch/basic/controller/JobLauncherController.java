package io.springbatch.springbatch.basic.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class JobLauncherController {

    private final Map<String, Job> jobMap;
    private final JobLauncher jobLauncher;
    // 비동기 TaskExecutor 설정을 위한 변수
    private final TaskExecutorJobLauncher taskExecutorJobLauncher;

    // 비동기 TaskExecutor 설정을 위한 생성자
    public JobLauncherController(Map<String, Job> jobMap, JobLauncher jobLauncher, DefaultBatchConfiguration defaultBatchConfiguration) {
        this.jobMap = jobMap;
        this.jobLauncher = jobLauncher;
        taskExecutorJobLauncher = (TaskExecutorJobLauncher) defaultBatchConfiguration.jobLauncher();
    }

    // 동기 실행을 위한 생성자
//    public JobLauncherController(Map<String, Job> jobMap, JobLauncher jobLauncher) {
//        this.jobMap = jobMap;
//        this.jobLauncher = jobLauncher;
//    }

    @PostMapping("/batch/sync")
    public String launchSync(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

        Job job = jobMap.get("jobLauncherJob");
        jobLauncher.run(job, jobParameters);
        return "batch completed";
    }

    @PostMapping("/batch/async")
    public String launchAsync(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .toJobParameters();

        Job job = jobMap.get("jobLauncherJob");
        taskExecutorJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        taskExecutorJobLauncher.run(job, jobParameters);

        return "batch completed";
    }
}
