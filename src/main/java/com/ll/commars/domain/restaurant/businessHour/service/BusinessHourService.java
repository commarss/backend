package com.ll.commars.domain.restaurant.businessHour.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateResponse;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourUpdateRequest;
import com.ll.commars.domain.restaurant.businessHour.entity.BusinessHour;
import com.ll.commars.domain.restaurant.businessHour.repository.jpa.BusinessHourRepository;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessHourService {

	private final RestaurantRepository restaurantRepository;
	private final BusinessHourRepository businessHourRepository;

	@Transactional
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

	@Transactional
	public void updateBusinessHour(Long restaurantId, Long businessHourId, BusinessHourUpdateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		BusinessHour businessHour = restaurant.getBusinessHours().stream()
			.filter(bh -> bh.getId().equals(businessHourId))
			.findFirst()
			.orElseThrow(() -> new CustomException(BUSINESS_HOUR_NOT_FOUND));

		businessHour.update(request.dayOfWeek(), request.openTime(), request.closeTime());
	}

	@Transactional
	public void deleteBusinessHour(Long restaurantId, Long businessHourId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		BusinessHour businessHour = restaurant.getBusinessHours().stream()
			.filter(bh -> bh.getId().equals(businessHourId))
			.findFirst()
			.orElseThrow(() -> new CustomException(BUSINESS_HOUR_NOT_FOUND));

		businessHourRepository.delete(businessHour);
	}
}
