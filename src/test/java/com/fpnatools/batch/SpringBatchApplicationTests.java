package com.fpnatools.batch;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application-test.yaml")
class SpringBatchApplicationTests {

	@Test
	void contextLoads() {
	} 

}
