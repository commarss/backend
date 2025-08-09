package com.ll.commars.global.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQLTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
		.withReuse(true);

	static {
		MYSQL_CONTAINER.start();
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		TestPropertyValues.of(
			"spring.datasource.url=" + MYSQL_CONTAINER.getJdbcUrl(),
			"spring.datasource.username=" + MYSQL_CONTAINER.getUsername(),
			"spring.datasource.password=" + MYSQL_CONTAINER.getPassword()
		).applyTo(applicationContext.getEnvironment());
	}
}
