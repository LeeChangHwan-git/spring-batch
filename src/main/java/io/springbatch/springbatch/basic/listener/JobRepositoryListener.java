package io.springbatch.springbatch.basic.listener;

import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.stereotype.Component;

@Component
public class JobRepositoryListener implements JobExecutionListener {

    private JobRepository jobRepository;

    public JobRepositoryListener(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobExecutionListener.super.beforeJob(jobExecution);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "2024.01.02")
                .toJobParameters();

        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if (lastJobExecution != null) {
            for (StepExecution execution : lastJobExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                ExitStatus exitStatus = execution.getExitStatus();
                String stepName = execution.getStepName();

                System.out.println("status = " + status);
                System.out.println("exitStatus = " + exitStatus);
                System.out.println("stepName = " + stepName);
            }
        }
    }
}
