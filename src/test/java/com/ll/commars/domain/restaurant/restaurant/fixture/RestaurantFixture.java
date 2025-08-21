package com.ll.commars.domain.restaurant.restaurant.fixture;

import static com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory.*;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class RestaurantFixture {

	private final FixtureMonkey fixtureMonkey;
	private final RestaurantRepository restaurantRepository;

	public Restaurant 한식_식당() {
		Restaurant restaurant =  fixtureMonkey.giveMeBuilder(Restaurant.class)
			.set("id", null)
			.set("name", "테스트 한식당")
			.set("details", "맛있는 한식당입니다")
			.set("address", "서울시 강남구")
			.set("restaurantCategory", 한식)
			.set("imageUrl", "https://example.com/image.jpg")
			.set("contact", "02-123-4567")
			.setNull("reviews")
			.setNull("menus")
			.setNull("favoriteRestaurants")
			.setNull("businessHours")
			.sample();

		return restaurantRepository.save(restaurant);
	}

	public Restaurant 중식_식당() {
		Restaurant restaurant = fixtureMonkey.giveMeBuilder(Restaurant.class)
			.set("id", null)
			.set("name", "테스트 중식당")
			.set("details", "맛있는 중식당입니다")
			.set("address", "서울시 강남구")
			.set("restaurantCategory", 중식)
			.set("imageUrl", "https://example.com/image.jpg")
			.set("contact", "02-123-4567")
			.setNull("reviews")
			.setNull("menus")
			.setNull("favoriteRestaurants")
			.setNull("businessHours")
			.sample();

		return restaurantRepository.save(restaurant);
	}

	public Restaurant 양식_식당() {
		Restaurant restaurant = fixtureMonkey.giveMeBuilder(Restaurant.class)
			.set("id", null)
			.set("name", "테스트 양식당")
			.set("details", "맛있는 양식당입니다")
			.set("address", "서울시 강남구")
			.set("restaurantCategory", 양식)
			.set("imageUrl", "https://example.com/image.jpg")
			.set("contact", "02-123-4567")
			.setNull("reviews")
			.setNull("menus")
			.setNull("favoriteRestaurants")
			.setNull("businessHours")
			.sample();

		return restaurantRepository.save(restaurant);
	}

	public Restaurant 일식_식당() {
		Restaurant restaurant = fixtureMonkey.giveMeBuilder(Restaurant.class)
			.set("id", null)
			.set("name", "테스트 일식당")
			.set("details", "맛있는 일식당입니다")
			.set("address", "서울시 강남구")
			.set("restaurantCategory", 일식)
			.set("imageUrl", "https://example.com/image.jpg")
			.set("contact", "02-123-4567")
			.setNull("reviews")
			.setNull("menus")
			.setNull("favoriteRestaurants")
			.setNull("businessHours")
			.sample();

		return restaurantRepository.save(restaurant);
	}
}
