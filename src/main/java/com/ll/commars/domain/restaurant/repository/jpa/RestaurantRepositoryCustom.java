package com.ll.commars.domain.restaurant.repository.jpa;

import java.util.List;

import com.ll.commars.domain.restaurant.dto.RestaurantSummaryResponse;

public interface RestaurantRepositoryCustom {

	List<RestaurantSummaryResponse> findAllSummary();
}
