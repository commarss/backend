package com.ll.commars.global.config;

import java.nio.file.Path;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String ELASTIC_OFFICIAL_IMAGE_NAME = "docker.elastic.co/elasticsearch/elasticsearch:8.3.3";
	private static final String CUSTOM_IMAGE_NAME = "commars-nori:8.3.3";
	private static final String DOCKERFILE_PATH = "src/test/resources/Dockerfile";
	private static final ElasticsearchContainer ELASTICSEARCH_CONTAINER = createAndStartContainer();

	private static ElasticsearchContainer createAndStartContainer() {
		// nori 플러그인이 존재하는 커스텀 도커 이미지 정의
		ImageFromDockerfile customImage = new ImageFromDockerfile(CUSTOM_IMAGE_NAME, false)
			.withDockerfile(Path.of(DOCKERFILE_PATH));

		DockerImageName compatibleImageName = DockerImageName.parse(customImage.get())
			.asCompatibleSubstituteFor(ELASTIC_OFFICIAL_IMAGE_NAME);

		ElasticsearchContainer container = new ElasticsearchContainer(compatibleImageName)
			.withEnv("xpack.security.enabled", "false")
			.withReuse(true);

		container.start();

		return container;
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		TestPropertyValues.of(
			"spring.elasticsearch.uris=" + ELASTICSEARCH_CONTAINER.getHttpHostAddress()
		).applyTo(applicationContext.getEnvironment());
	}
}
