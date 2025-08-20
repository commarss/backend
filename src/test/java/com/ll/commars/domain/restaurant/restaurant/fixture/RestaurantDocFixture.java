package com.ll.commars.domain.restaurant.restaurant.fixture;

import java.util.UUID;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurant.repository.elasticsearch.RestaurantDocRepository;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@RequiredArgsConstructor
public class RestaurantDocFixture {

	private final FixtureMonkey fixtureMonkey;
	private final RestaurantDocRepository restaurantDocRepository;

	public RestaurantDoc 식당(
		String name, String details, RestaurantCategory category, double lat,
		double lon, double averageRate
	) {
		RestaurantDoc restaurant = fixtureMonkey.giveMeBuilder(RestaurantDoc.class)
			.set("id", UUID.randomUUID().toString())
			.set("name", name)
			.set("details", details)
			.set("restaurantCategory", category)
			.set("location", new GeoPoint(lat, lon))
			.set("averageRate", averageRate)
			.sample();

		return restaurantDocRepository.save(restaurant);
	}
}
