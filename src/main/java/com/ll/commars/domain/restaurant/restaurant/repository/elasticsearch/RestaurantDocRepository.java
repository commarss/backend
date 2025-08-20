package com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;

public interface RestaurantDocRepository
	extends ElasticsearchRepository<RestaurantDoc, String>, RestaurantDocRepositoryCustom {

	List<RestaurantDoc> findTop5ByOrderByAverageRateDesc();
}
