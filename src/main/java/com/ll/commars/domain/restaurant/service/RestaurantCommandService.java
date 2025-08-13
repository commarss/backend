package com.ll.commars.domain.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateResponse;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantCommandService {

	private final RestaurantRepository restaurantRepository;

	@Transactional
	public RestaurantCreateResponse createRestaurant(RestaurantCreateRequest request) {
		RestaurantCategory restaurantCategory = RestaurantCategory.fromString(request.category());

		Restaurant restaurant = new Restaurant(request.name(), request.details(),
			request.imageUrl(), request.contact(), request.address(), restaurantCategory);

		// todo: 추후 주소를 토대로 lat, lng을 계산하는 API 호출 로직 구현 - 스프링 이벤트 사용

		return RestaurantCreateResponse.from(restaurantRepository.save(restaurant));
	}

	@Transactional
	public RestaurantUpdateResponse updateRestaurant(Long restaurantId,
		RestaurantUpdateRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		restaurant.updateRestaurant(request.restaurantName(), request.category());

		return RestaurantUpdateResponse.from(restaurantRepository.save(restaurant));
	}

	@Transactional
	public void deleteRestaurant(Long restaurantId) {
		restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		restaurantRepository.deleteById(restaurantId);
	}
}
