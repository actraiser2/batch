package com.fpnatools.batch.jobs;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class HelloWorldJob {

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step helloWorlStep() {
		return stepBuilderFactory.get("step1").tasklet(helloWorldTaskLet(null, null)).build();
	}
	
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("helloWorld").start(helloWorlStep()).
				incrementer(jobParametersIncerementer()).
				build();
	}
	
	@Bean
	@StepScope()
	public Tasklet helloWorldTaskLet(@Value("#{jobParameters['startDate']}") String startDate,
			@Value("#{jobParameters['timestamp']}") Date timestamp){
		return (stepContr, chunkContext) -> {
				log.info("Executing taskelet with startDate:" + startDate);
				log.info("Executing taskelet with timestamp:" + timestamp);
				return RepeatStatus.FINISHED;
			};
	}
	
	
	@Bean
	public JobParametersIncrementer jobParametersIncerementer() {
		return parameters -> {
			 return new JobParametersBuilder(parameters)
			            .addDate("timestamp", new Date())
			            .toJobParameters();
		};
	}
	
}
