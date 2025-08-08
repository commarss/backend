package com.ll.commars.global.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class RedisTestContainerConfig {

	private static final int REDIS_PORT = 6379;

	private static final String REDIS_DOCKER_IMAGE = "redis:7-alpine";
	private static final GenericContainer<?> REDIS_CONTAINER;


	static {
		REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
			.withExposedPorts(6379)
			.withReuse(true);
		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(REDIS_PORT).toString());
	}
}
