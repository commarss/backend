package com.ll.commars.global.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.global.config.MySQLTestContainerConfig;
import com.ll.commars.global.config.RedisTestContainerConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import({
	RedisTestContainerConfig.class,
	MySQLTestContainerConfig.class
})
public @interface IntegrationTest {
}
