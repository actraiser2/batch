package com.fpnatools.batch.jobs.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration extends DefaultTaskConfigurer {

	public TaskConfiguration(@Autowired DataSource dataflowDataSource) {
		super(dataflowDataSource);
	}
	

}
