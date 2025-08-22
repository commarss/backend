package com.ll.commars.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.ll.commars.global.config.DatabaseClearExtension;
import com.ll.commars.global.config.ElasticsearchTestContainer;
import com.ll.commars.global.config.FixtureMonkeyConfig;
import com.ll.commars.global.config.MySQLTestContainer;
import com.ll.commars.global.config.RedisTestContainer;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@ExtendWith({DatabaseClearExtension.class})
@Import(FixtureMonkeyConfig.class)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {
	RedisTestContainer.class,
	MySQLTestContainer.class,
	ElasticsearchTestContainer.class
})
public @interface IntegrationTest {
}
