package com.ll.commars.domain.restaurant.restaurant.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSearchResponse;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch.RestaurantDocRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantDocService {

	private final RestaurantDocRepository restaurantDocRepository;
	private final ElasticsearchClient elasticsearchClient;

	@Transactional(readOnly = true)
	public RestaurantSearchResponse searchRestaurants(String keyword, Double lat, Double lon, Double distance) {
		List<RestaurantDoc> restaurantDocs = restaurantDocRepository.searchByKeywordAndLocation(
			keyword,
			lat,
			lon,
			distance
		);

		return RestaurantSearchResponse.from(restaurantDocs);
	}

	public List<RestaurantDoc> showSortByRate() {
		return restaurantDocRepository.findTop5ByOrderByAverageRateDesc();
	}

	public List<RestaurantDoc> findNearbyRestaurants(Double lat, Double lon, Double distance) {
		try {
			// GeoDistance 쿼리 생성
			Query geoDistanceQuery = GeoDistanceQuery.of(g -> g
				.field("location")
				.distance(distance + "km")
				.location(l -> l.latlon(ll -> ll
					.lat(lat)
					.lon(lon)
				))
			)._toQuery();

			// 검색 요청 생성
			SearchRequest searchRequest = SearchRequest.of(s -> s
				.index("es_restaurants")
				.query(geoDistanceQuery)
				.size(10)
			);

			// 검색 실행
			SearchResponse<RestaurantDoc> response = elasticsearchClient.search(searchRequest, RestaurantDoc.class);

			// 결과 반환
			return response.hits().hits().stream()
				.map(hit -> hit.source())
				.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException("Failed to search nearby restaurants", e);
		}
	}
}
