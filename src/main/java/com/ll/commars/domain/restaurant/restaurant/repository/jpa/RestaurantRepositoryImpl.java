package com.ll.commars.domain.restaurant.restaurant.repository.jpa;

import static com.ll.commars.domain.restaurant.restaurant.entity.QRestaurant.*;

import java.util.List;

import com.ll.commars.domain.restaurant.restaurant.dto.QRestaurantSummaryResponse;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryResponse;
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
				restaurant.address,
				restaurant.restaurantCategory
			))
			.from(restaurant)
			.orderBy(restaurant.id.asc())
			.fetch();
	}
}
