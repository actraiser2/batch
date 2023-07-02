package com.fpnatools.batch.jobs.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class DatabaseConfiguration {

	@Bean
	@ConfigurationProperties("spring.datasource.dataflow")
	public DataSourceProperties dataflowProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	@ConfigurationProperties("spring.datasource.jhipster")
	public DataSourceProperties jhipsterProperties() {
		return new DataSourceProperties();
	}
	
	@Bean
	@Primary
	public DataSource dataflowDataSource() {
		return dataflowProperties().initializeDataSourceBuilder().build();
	}
	
	@Bean
	public DataSource jhipsterDataSource() {
		return jhipsterProperties().initializeDataSourceBuilder().build();
	}
	
	@Bean
    public TransactionManager  dataflowTransactionManager() { 
        return new JdbcTransactionManager(dataflowDataSource());                              
    }
 
	@Bean
    public JpaTransactionManager jhipsterTransactionManager(EntityManagerFactory emf) { 
        return new JpaTransactionManager(emf);                              
    }
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		var emf = new LocalContainerEntityManagerFactoryBean(); 
		emf.setDataSource(jhipsterDataSource());
		emf.setJpaVendorAdapter(jpaVendorAdapter());
		emf.setPackagesToScan("com.fpnatools.batch.domain");
		return emf;
	}
	
	@Bean
    public JpaVendorAdapter jpaVendorAdapter() {                            
        HibernateJpaVendorAdapter jpaVendorAdapter = new  HibernateJpaVendorAdapter();                               
        jpaVendorAdapter.setDatabase(Database.POSTGRESQL);                       
        jpaVendorAdapter.setShowSql(false);
        jpaVendorAdapter.setGenerateDdl(true);
        return jpaVendorAdapter;                                            
    }
}
