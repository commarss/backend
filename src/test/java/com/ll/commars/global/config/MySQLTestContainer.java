package com.ll.commars.global.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQLTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String MYSQL_IMAGE_NAME = "mysql:8.0";
	private static final String DATABASE_NAME = "commars_test";
	private static final String MYSQL_USERNAME = "testuser";
	private static final String MYSQL_PASSWORD = "testpassword";
	private static final MySQLContainer<?> MYSQL_CONTAINER = createAndStartContainer();

	private static MySQLContainer<?> createAndStartContainer() {
		MySQLContainer<?> container = new MySQLContainer<>(DockerImageName.parse(MYSQL_IMAGE_NAME))
			.withDatabaseName(DATABASE_NAME)
			.withUsername(MYSQL_USERNAME)
			.withPassword(MYSQL_PASSWORD)
			.withReuse(true);

		container.start();

		return container;
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
