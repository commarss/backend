package com.ll.commars.domain.restaurant.repository.jpa;

import java.util.List;

import com.ll.commars.domain.restaurant.dto.RestaurantSummaryResponse;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RestaurantSummaryResponse> findAllSummary() {
		return queryFactory
			.select(new QRestaurantSummaryResponse(
				restaurant.id,
				restaurant.name,
				restaurant.imageUrl,
				restaurant.averageRate,
				restaurant.restaurantCategory,
				restaurant.address
			))
			.from(restaurant)
			.orderBy(restaurant.id.asc())
			.fetch();
	}
}
