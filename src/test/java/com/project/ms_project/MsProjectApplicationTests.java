package com.project.ms_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MsProjectApplicationTests {

	@Test
	void contextLoads() {
		assertNotNull("El contexto no es nulo");
	}

}
