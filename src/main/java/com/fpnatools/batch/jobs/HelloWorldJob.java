package com.fpnatools.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
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
		return stepBuilderFactory.get("step1").tasklet(helloWorldTaskLet(null)).build();
	}
	
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("helloWorld").start(helloWorlStep()).
				build();
	}
	
	@Bean
	@StepScope()
	public Tasklet helloWorldTaskLet(@Value("#{jobParameters['startDate']}") String startDate){
		return (stepContr, chunkContext) -> {
				log.info("Executing taskelet wirh date startDate:" + startDate);
				return RepeatStatus.FINISHED;
			};
	}
	
	
}
