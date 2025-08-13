package com.ll.commars.domain.restaurant.businessHour.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateResponse;
import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessHourService {

	private final RestaurantRepository restaurantRepository;

	public BusinessHourBulkCreateResponse createBusinessHours(Long restaurantId, BusinessHourBulkCreateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		List<BusinessHour> newHours = request.businessHours().stream()
			.map(dto -> new BusinessHour(
				dto.dayOfWeek(),
				dto.openTime(),
				dto.closeTime(),
				restaurant
			))
			.toList();

		restaurant.updateBusinessHours(newHours);

		return new BusinessHourBulkCreateResponse(restaurant.getId(), newHours.size());
	}
}
