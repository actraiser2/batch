package com.fpnatools.batch.jobs;

import java.util.Date;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

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
	public Step helloWorlChunk() {
		return stepBuilderFactory.get("step1").
				<String, String>chunk(20).
				reader(itemReader(null)).
				writer(itemWriter()).build();
	}
	
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("helloWorld").
				start(helloWorlChunk()).
				incrementer(jobParametersIncerementer()).
				on(ExitStatus.COMPLETED.getExitCode()).
				to(helloWorlStep()).end().
				
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
	@StepScope
	public FlatFileItemReader<String> itemReader(ResourceLoader resourceLoader) {
		log.info("ResourceLoader:" + resourceLoader);
		var itemReader = new FlatFileItemReader<String>();
		itemReader.setResource(resourceLoader.getResource("classpath:/providers.csv"));
		itemReader.setLineMapper(new DefaultLineMapper<>() {

			@Override
			public String mapLine(String line, int lineNumber) throws Exception {
				// TODO Auto-generated method stub
				return lineNumber + ";" + line;
			}
			
		});
		return itemReader;
	}
	
	@Bean
	public ItemWriter<String> itemWriter() {
		return items -> {
			items.stream().forEach(item -> {
				log.info("Item received:" + item.length());
			});
			
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
