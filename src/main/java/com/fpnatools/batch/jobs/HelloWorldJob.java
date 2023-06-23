package com.fpnatools.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class HelloWorldJob {

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").tasklet((stepContr, chunckContext) -> {
			log.info("Executing step1");
			return RepeatStatus.FINISHED;
		}).build();
	}
	
	
	@Bean
	public Job job(Step step1) {
		return jobBuilderFactory.get("job-helloWorld").start(step1).build();
	}
}
