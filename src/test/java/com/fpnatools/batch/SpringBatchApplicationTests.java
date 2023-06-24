package com.fpnatools.batch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.properties")
@Slf4j
class SpringBatchApplicationTests {

	@Autowired JobLauncher jobLauncher;
	@Autowired Job job;
	@Autowired Environment env;
	
	@Test
	void contextLoads() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		var jobParameters = new JobParametersBuilder().addString("startDate", 
				LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).
			toJobParameters();
		
		jobLauncher.run(job, jobParameters);
		
		log.info("Env:" + env.getProperty("spring.datasource.url"));
	} 

}
