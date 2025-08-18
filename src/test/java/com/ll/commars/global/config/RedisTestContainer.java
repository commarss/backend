package com.ll.commars.global.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class RedisTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String REDIS_DOCKER_IMAGE = "redis:7-alpine";
	private static final int REDIS_PORT = 6379;
	private static final GenericContainer<?> REDIS_CONTAINER = createAndStartContainer();

	private static GenericContainer<?> createAndStartContainer() {
		GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
			.withExposedPorts(REDIS_PORT)
			.waitingFor(Wait.forListeningPort())
			.withReuse(true);

		container.start();

		return container;
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		TestPropertyValues.of(
			"spring.data.redis.host=" + REDIS_CONTAINER.getHost(),
			"spring.data.redis.port=" + REDIS_CONTAINER.getMappedPort(REDIS_PORT)
		).applyTo(applicationContext.getEnvironment());
	}
}
