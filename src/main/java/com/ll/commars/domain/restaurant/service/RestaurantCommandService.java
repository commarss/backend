package com.ll.commars.domain.restaurant.service;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.member.service.MemberService;
import com.ll.commars.domain.restaurant.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantCreateResponse;
import com.ll.commars.domain.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateRequest;
import com.ll.commars.domain.restaurant.dto.RestaurantUpdateResponse;
import com.ll.commars.domain.restaurant.entity.BusinessHour;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.repository.jpa.BusinessHourRepository;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;
import com.ll.commars.global.exception.CustomException;
import com.ll.commars.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantCommandService {

	private final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;
	private final MemberRepository memberRepository;
	private final BusinessHourRepository businessHourRepository;
	private final MemberService memberService;

	@Transactional
	public RestaurantCreateResponse createRestaurant(RestaurantCreateRequest request) {
		RestaurantCategory restaurantCategory = RestaurantCategory.fromString(request.category());

		Restaurant restaurant = new Restaurant(request.name(), request.details(),
			request.imageUrl(), request.contact(), request.address(), restaurantCategory);

		// todo: 추후 주소를 토대로 lat, lng을 계산하는 API 호출 로직 구현 - 스프링 이벤트 사용

		return RestaurantCreateResponse.from(restaurantRepository.save(restaurant));
	}

	// 식당 리뷰 작성
	@Transactional
	public ReviewDto.ReviewWriteResponse writeReview(Long restaurantId, ReviewDto.ReviewWriteRequest request,
		Long userId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		Optional<Member> user = memberService.findById(userId);

		reviewRepository.save(Review.builder()
			.restaurant(restaurant)
			.member(user.get())
			.body(request.getBody())
			.name(request.getReviewName())
			.rate(request.getRate())
			.build());

		// 해당 식당의 모든 리뷰 평점 평균 계산
		List<Review> allReviews = reviewRepository.findByRestaurantId(restaurantId);
		double newAverageRate = allReviews.stream()
			.mapToInt(Review::getRate)
			.average()
			.orElse(0.0);
		System.out.println("newAverageRate = " + newAverageRate);

		// 식당의 평균 평점 업데이트
		restaurant.setAverageRate(newAverageRate);
		restaurantRepository.save(restaurant);

		return ReviewDto.ReviewWriteResponse.builder()
			.userName(user.get().getName())
			.reviewName(request.getReviewName())
			.restaurantName(restaurant.getName())
			.body(request.getBody())
			.rate(request.getRate())
			.build();
	}

	public void deleteRestaurant(Long restaurantId) {
		restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		restaurantRepository.deleteById(restaurantId);
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
	public RestaurantDto.RestaurantCategoryWriteResponse writeCategory(Long restaurantId,
		RestaurantCategoryDto.RestaurantCategoryWriteRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		RestaurantCategory category = restaurantCategoryRepository.findById(request.getId())
			.orElseThrow(() -> new IllegalArgumentException("Category not found"));

		restaurant.setCategory(category);

		restaurantRepository.save(restaurant);

		return RestaurantDto.RestaurantCategoryWriteResponse.builder()
			.restaurantName(restaurant.getName())
			.categoryName(category.getName())
			.build();
	}

	@Transactional(readOnly = true)
	public RestaurantCategoryDto.ShowCategoryNameResponse getCategories(Long restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		RestaurantCategory category = restaurant.getRestaurantCategory();

		return RestaurantCategoryDto.ShowCategoryNameResponse.builder()
			.categoryName(category.getName())
			.build();
	}

	@Transactional
	public RestaurantDto.RestaurantCategoryWriteResponse modifyCategory(Long restaurantId,
		RestaurantCategoryDto.RestaurantCategoryWriteRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		RestaurantCategory category = restaurantCategoryRepository.findById(request.getId())
			.orElseThrow(() -> new IllegalArgumentException("Category not found"));

		restaurant.setRestaurantCategory(category);

		return RestaurantDto.RestaurantCategoryWriteResponse.builder()
			.restaurantName(restaurant.getName())
			.categoryName(category.getName())
			.build();
	}

	@Transactional
	public BusinessHourDto.BusinessHourWriteResponse writeBusinessHours(
		Long restaurantId,
		BusinessHourDto.BusinessHourWriteRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		List<BusinessHour> businessHours = request.getBusinessHours().stream()
			.map(businessHour -> BusinessHour.builder()
				.dayOfWeek(businessHour.getDayOfWeek())
				.openTime(businessHour.getOpenTime())
				.closeTime(businessHour.getCloseTime())
				.restaurant(restaurant)
				.build())
			.collect(Collectors.toList());

		restaurant.setBusinessHours(businessHours);
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);

		return BusinessHourDto.BusinessHourWriteResponse.builder()
			.restaurantName(savedRestaurant.getName())
			.businessHours(savedRestaurant.getBusinessHours().stream()
				.map(businessHour -> BusinessHourDto.BusinessHourInfo.builder()
					.id(businessHour.getId())
					.dayOfWeek(businessHour.getDayOfWeek())
					.openTime(businessHour.getOpenTime())
					.closeTime(businessHour.getCloseTime())
					.build())
				.collect(Collectors.toList()))
			.build();
	}

	@Transactional
	public BusinessHourDto.BusinessHourWriteResponse modifyBusinessHours(Long restaurantId,
		BusinessHourDto.BusinessHourWriteRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		List<BusinessHour> businessHours = request.getBusinessHours().stream()
			.map(businessHour -> BusinessHour.builder()
				.dayOfWeek(businessHour.getDayOfWeek())
				.openTime(businessHour.getOpenTime())
				.closeTime(businessHour.getCloseTime())
				.restaurant(restaurant)
				.build())
			.collect(Collectors.toList());

		restaurant.setBusinessHours(businessHours);
		Restaurant savedRestaurant = restaurantRepository.save(restaurant);

		return BusinessHourDto.BusinessHourWriteResponse.builder()
			.restaurantName(savedRestaurant.getName())
			.businessHours(savedRestaurant.getBusinessHours().stream()
				.map(businessHour -> BusinessHourDto.BusinessHourInfo.builder()
					.id(businessHour.getId())
					.dayOfWeek(businessHour.getDayOfWeek())
					.openTime(businessHour.getOpenTime())
					.closeTime(businessHour.getCloseTime())
					.build())
				.collect(Collectors.toList()))
			.build();
	}
}
