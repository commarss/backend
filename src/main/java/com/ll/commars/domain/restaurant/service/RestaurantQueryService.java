package com.ll.commars.domain.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.dto.RestaurantFindListResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantFindResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantSummaryResponse;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.global.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

	private final RestaurantRepository restaurantRepository;

	@Transactional(readOnly = true)
	public RestaurantFindListResponse getRestaurants() {
		List<RestaurantSummaryResponse> summarizedRestaurants = restaurantRepository.findAllSummary();

		return new RestaurantFindListResponse(summarizedRestaurants);
	}

	@Transactional(readOnly = true)
	public ReviewDto.ShowAllReviewsResponse getReviews(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		List<ReviewDto.ReviewInfo> reviewInfos = restaurant.getReviews().stream()
			.map(review -> ReviewDto.ReviewInfo.builder()
				.userId(review.getMember().getId())
				.restaurantId(review.getRestaurant().getId())
				.id(review.getId())
				.name(review.getName())
				.body(review.getBody())
				.rate(review.getRate())
				.build())
			.collect(Collectors.toList());

		return ReviewDto.ShowAllReviewsResponse.builder()
			.reviews(reviewInfos)
			.build();
	}

	@Transactional(readOnly = true)
	public RestaurantFindResponse getRestaurant(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

		return RestaurantFindResponse.from(restaurant);
	}
}
