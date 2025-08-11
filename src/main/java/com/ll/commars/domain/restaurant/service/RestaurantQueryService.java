package com.ll.commars.domain.restaurant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.restaurant.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantQueryService {

	private final RestaurantRepository restaurantRepository;

	// 모든 식당 조회
	@Transactional(readOnly = true)
	public RestaurantDto.RestaurantShowAllResponse getRestaurants() {
		List<Restaurant> restaurants = restaurantRepository.findAllWithMenus();

		List<RestaurantDto.RestaurantInfo> restaurantInfos = restaurants.stream()
			.map(restaurant -> {
				List<RestaurantMenuDto.MenuInfo> menuInfos = restaurant.getRestaurantMenus().stream()
					.map(menu -> RestaurantMenuDto.MenuInfo.builder()
						.name(menu.getName())
						.price(menu.getPrice())
						.imageUrl(menu.getImageUrl())
						.build())
					.collect(Collectors.toList());

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

				List<BusinessHourDto.BusinessHourInfo> businessHourInfos = restaurant.getBusinessHours().stream()
					.map(businessHour -> BusinessHourDto.BusinessHourInfo.builder()
						.id(businessHour.getId())
						.dayOfWeek(businessHour.getDayOfWeek())
						.openTime(businessHour.getOpenTime())
						.closeTime(businessHour.getCloseTime())
						.build())
					.toList();

				return RestaurantDto.RestaurantInfo.builder()
					.id(restaurant.getId())
					.name(restaurant.getName())
					.details(restaurant.getDetails())
					.averageRate(restaurant.getAverageRate())
					.imageUrl(restaurant.getImageUrl())
					.contact(restaurant.getContact())
					.address(restaurant.getAddress())
					.lat(restaurant.getLat())
					.lon(restaurant.getLon())
					.runningState(restaurant.getRunningState())
					.summarizedReview(restaurant.getSummarizedReview())
					.categoryId(restaurant.getRestaurantCategory().getId())
					.restaurantMenus(menuInfos)
					.reviews(reviewInfos)
					.businessHours(businessHourInfos)
					.build();
			})
			.collect(Collectors.toList());

		return RestaurantDto.RestaurantShowAllResponse.builder()
			.restaurants(restaurantInfos)
			.build();
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

	@Transactional
	public RestaurantMenuDto.ShowAllMenusResponse getMenus(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		List<RestaurantMenuDto.MenuInfo> menuInfos = restaurant.getRestaurantMenus().stream()
			.map(menu -> RestaurantMenuDto.MenuInfo.builder()
				.name(menu.getName())
				.price(menu.getPrice())
				.imageUrl(menu.getImageUrl())
				.build())
			.collect(Collectors.toList());

		return RestaurantMenuDto.ShowAllMenusResponse.builder()
			.menus(menuInfos)
			.build();
	}

	@Transactional(readOnly = true)
	public RestaurantDto.RestaurantInfo getRestaurant(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		List<RestaurantMenuDto.MenuInfo> menuInfos = restaurant.getRestaurantMenus().stream()
			.map(menu -> RestaurantMenuDto.MenuInfo.builder()
				.id(menu.getId())
				.name(menu.getName())
				.price(menu.getPrice())
				.imageUrl(menu.getImageUrl())
				.build())
			.collect(Collectors.toList());

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

		List<BusinessHourDto.BusinessHourInfo> businessHourInfos = restaurant.getBusinessHours().stream()
			.map(businessHour -> BusinessHourDto.BusinessHourInfo.builder()
				.id(businessHour.getId())
				.dayOfWeek(businessHour.getDayOfWeek())
				.openTime(businessHour.getOpenTime())
				.closeTime(businessHour.getCloseTime())
				.build())
			.toList();

		return RestaurantDto.RestaurantInfo.builder()
			.id(restaurant.getId())
			.name(restaurant.getName())
			.details(restaurant.getDetails())
			.averageRate(restaurant.getAverageRate())
			.imageUrl(restaurant.getImageUrl())
			.contact(restaurant.getContact())
			.address(restaurant.getAddress())
			.lat(restaurant.getLat())
			.lon(restaurant.getLon())
			.runningState(restaurant.getRunningState())
			.summarizedReview(restaurant.getSummarizedReview())
			.categoryId(restaurant.getRestaurantCategory().getId())
			.restaurantMenus(menuInfos)
			.reviews(reviewInfos)
			.businessHours(businessHourInfos)
			.build();
	}
}
