package io.springbatch.springbatch.basic.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String stepName = contribution.getStepExecution().getStepName();
        String jobName = chunkContext.getStepContext().getJobName();
        System.out.println(">>>CustomTasklet Info");
        System.out.println("jobName = " + jobName);
        System.out.println("stepName = " + stepName);

        return RepeatStatus.FINISHED;
    }
}
