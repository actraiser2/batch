package com.fpnatools.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableBatchProcessing()
@EnableTask()
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
