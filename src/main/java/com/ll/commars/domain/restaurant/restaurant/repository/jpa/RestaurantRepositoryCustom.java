package com.ll.commars.domain.restaurant.restaurant.repository.jpa;

import java.util.List;

import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantSummaryResponse;
import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantCategory;

public interface RestaurantRepositoryCustom {

	List<RestaurantSummaryResponse> findAllSummary();
	List<RestaurantSummaryResponse> findByCategorySummary(RestaurantCategory category);
}
