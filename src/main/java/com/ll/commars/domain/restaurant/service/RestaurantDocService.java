package com.ll.commars.domain.restaurant.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ll.commars.domain.restaurant.entity.RestaurantDoc;
import com.ll.commars.domain.restaurant.repository.elasticsearch.RestaurantDocRepository;

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

	public RestaurantDoc write(String name, String details, Double averageRate, Double lat, Double lon) {
		RestaurantDoc restaurantDoc = RestaurantDoc.builder()
			.name(name)
			.details(details)
			.averageRate(averageRate)
			.location(lat + "," + lon)
			.build();

		return restaurantDocRepository.save(restaurantDoc);
	}

	public void truncate() {
		restaurantDocRepository.deleteAll();
	}

	public List<RestaurantDoc> searchByKeyword(String keyword, double userLat, double userLng, String distance) {
		try {
			if (keyword == null || keyword.trim().isEmpty()) {
				System.out.println("empty keyword");
				return List.of();
			}
			return restaurantDocRepository.searchByKeywordAndLocation(
				keyword.trim(),
				userLat,
				userLng,
				distance
			);
		} catch (Exception e) {
			System.out.println("e = " + e);
			return List.of();
		}
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
