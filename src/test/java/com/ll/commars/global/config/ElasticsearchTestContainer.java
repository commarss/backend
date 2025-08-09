package com.ll.commars.global.config;

import java.nio.file.Path;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchTestContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static final String ELASTIC_OFFICIAL_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:8.3.3";

	private static final ElasticsearchContainer ELASTICSEARCH_CONTAINER =
		new ElasticsearchContainer(
			DockerImageName.parse(new ImageFromDockerfile("commars-nori", false)
					.withDockerfile(Path.of("src/test/resources/Dockerfile"))
					.get())
				.asCompatibleSubstituteFor(ELASTIC_OFFICIAL_IMAGE)
		)
			.withEnv("xpack.security.enabled", "false")
			.withReuse(true);


	static {
		ELASTICSEARCH_CONTAINER.start();
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		TestPropertyValues.of(
			"spring.elasticsearch.uris=" + ELASTICSEARCH_CONTAINER.getHttpHostAddress()
		).applyTo(applicationContext.getEnvironment());
	}
}
