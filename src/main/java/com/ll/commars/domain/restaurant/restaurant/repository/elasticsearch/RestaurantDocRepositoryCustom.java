package com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch;

import java.util.List;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;

public interface RestaurantDocRepositoryCustom {
	List<RestaurantDoc> searchByKeywordAndLocation(String keyword, GeoPoint location, double distance);

	List<RestaurantDoc> findNearbyRestaurantsSortedByDistance(GeoPoint location, double distance);
}
