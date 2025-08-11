package com.ll.commars.global.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;

import com.ll.commars.domain.restaurant.entity.RestaurantDoc;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfig {

	private final ElasticsearchOperations elasticsearchOperations;

	@EventListener(ApplicationReadyEvent.class)
	public void initIndices() {
		try {
			IndexOperations indexOperations = elasticsearchOperations.indexOps(RestaurantDoc.class);

			// 기존 인덱스가 있다면 삭제
			if (indexOperations.exists()) {
				indexOperations.delete();
			}

			// 인덱스 생성
			indexOperations.create();

			// 설정 및 매핑 적용
			Document settings = indexOperations.createMapping(RestaurantDoc.class);
			indexOperations.putMapping(settings);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize Elasticsearch indices", e);
		}
	}
}
