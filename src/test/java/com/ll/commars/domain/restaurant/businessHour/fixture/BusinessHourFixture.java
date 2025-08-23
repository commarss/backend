package com.ll.commars.domain.restaurant.businessHour.fixture;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.springframework.boot.test.context.TestComponent;

import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.businessHour.repository.jpa.BusinessHourRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;

import lombok.RequiredArgsConstructor;

import com.navercorp.fixturemonkey.FixtureMonkey;

@TestComponent
@RequiredArgsConstructor
public class BusinessHourFixture {

	private final FixtureMonkey fixtureMonkey;
	private final BusinessHourRepository businessHourRepository;

	public BusinessHour 영업시간(Restaurant restaurant, DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
		BusinessHour businessHour = fixtureMonkey.giveMeBuilder(BusinessHour.class)
			.set("id", null)
			.set("restaurant", restaurant)
			.set("dayOfWeek", dayOfWeek)
			.set("openTime", openTime)
			.set("closeTime", closeTime)
			.sample();

		return businessHourRepository.save(businessHour);
	}
}
