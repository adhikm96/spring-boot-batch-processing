package com.batch.processing;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BatchProcessingApplication {

    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        ApplicationContext context = SpringApplication.run(BatchProcessingApplication.class, args);

        JobOperator jobOperator = context.getBean(JobOperator.class);
        Job job = context.getBean(Job.class);

		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();

		JobExecution  execution = jobOperator.start(job, jobParameters);

		// Optional: print status
		System.out.println("Job Status: " + execution.getStatus());
	}



}
