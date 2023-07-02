package com.fpnatools.batch.jobs;

import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.fpnatools.batch.domain.ProviderEntity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@AllArgsConstructor
@Slf4j
public class HelloWorldJob {

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;
	private EntityManagerFactory emf;
	private JpaTransactionManager jpaTransactionManager;
	
	@Bean
	public Step helloWorlStep() {
		return stepBuilderFactory.get("step1").tasklet(helloWorldTaskLet(null, null)).build();
	}
	
	@Bean
	public Step helloWorlChunk() {
		return stepBuilderFactory.get("step1").
				transactionManager(jpaTransactionManager).
				<ProviderEntity, ProviderEntity>chunk(20).
				reader(itemReader(null)).
				processor(itemProcessor()).
				writer(itemWriter( )).build();
	}
	
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("helloWorld").
				start(helloWorlChunk()).
				incrementer(jobParametersIncerementer()).
				on(ExitStatus.COMPLETED.getExitCode()).
				to(helloWorlStep()).
				end().
				
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
	public FlatFileItemReader<ProviderEntity> itemReader(ResourceLoader resourceLoader) {
		log.info("ResourceLoader:" + resourceLoader);
		var itemReader = new FlatFileItemReader<ProviderEntity>();
		itemReader.setResource(resourceLoader.getResource("classpath:/providers.csv"));
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(new DefaultLineMapper<>() {

			@Override
			public ProviderEntity mapLine(String line, int lineNumber) throws Exception {
				// TODO Auto-generated method stub
				String[] tokens = line.split("\\t");
				if (tokens.length < 10 || !NumberUtils.isParsable(tokens[0])) return ProviderEntity.builder().build();
				//log.info(Arrays.asList(tokens) + "");
				Long providerId = Long.parseLong(tokens[0]);
				String address = tokens[3];
				String providerName = tokens[9];
				return ProviderEntity.builder().
					address(address).
					providerId(providerId).
					providerName(providerName).
					build();
			}
			
		});
		return itemReader;
	}
	
	
	

	public ItemWriter<ProviderEntity> itemWriter() {

		JpaItemWriter<ProviderEntity> itemWriter = new JpaItemWriter<>();
		itemWriter.setEntityManagerFactory(emf);
		return itemWriter;
	}
	
	public ItemProcessor<ProviderEntity, ProviderEntity> itemProcessor(){
		return p -> {
			return p.getProviderId() != null ? p : null;
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
