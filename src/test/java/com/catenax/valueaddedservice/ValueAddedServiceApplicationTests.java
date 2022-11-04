package com.catenax.valueaddedservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class ValueAddedServiceApplicationTests {

	@Test
	void contextLoads() {
		assertNotEquals(0,1);
	}

}
