// package com.ll.commars.global.config;
//
// import java.util.List;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
//
// import com.ll.commars.global.converter.GeoPointToStringConverter;
//
// @Configuration
// public class ElasticsearchConfig {
//
// 	@Bean
// 	public ElasticsearchCustomConversions elasticsearchCustomConversions() {
// 		return new ElasticsearchCustomConversions(
// 			List.of(new GeoPointToStringConverter())
// 		);
// 	}
// }
