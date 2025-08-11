package com.ll.commars.domain.restaurant.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ll.commars.domain.member.entity.Member;
import com.ll.commars.domain.member.repository.jpa.MemberRepository;
import com.ll.commars.domain.member.service.MemberService;
import com.ll.commars.domain.restaurant.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.entity.BusinessHour;
import com.ll.commars.domain.restaurant.repository.jpa.BusinessHourRepository;
import com.ll.commars.domain.restaurant.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantCategoryRepository;
import com.ll.commars.domain.restaurant.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.repository.jpa.RestaurantRepository;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.repository.jpa.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;
	private final ReviewRepository reviewRepository;
	private final MemberRepository memberRepository;
	private final RestaurantCategoryRepository restaurantCategoryRepository;
	private final BusinessHourRepository businessHourRepository;
	private final MemberService memberService;

	// 식당 정보 등록
	@Transactional
	public RestaurantDto.RestaurantWriteResponse write(
		RestaurantDto.RestaurantWriteRequest request
	) {
		// 카테고리가 존재하지 않을 경우 예외 처리
		RestaurantCategory category = restaurantCategoryRepository.findById(request.getCategoryId())
			.orElseThrow(() -> new IllegalArgumentException("Category not found"));

		// request로 받은 정보로 식당 객체 생성
		Restaurant restaurant = Restaurant.builder()
			.name(request.getName())
			.details(request.getDetails())
			.averageRate(request.getAverageRate())
			.imageUrl(request.getImageUrl())
			.contact(request.getContact())
			.address(request.getAddress())
			.lat(request.getLat())
			.lon(request.getLon())
			.runningState(request.getRunningState())
			.summarizedReview(request.getSummarizedReview())
			.build();

		restaurant.setCategory(category);
		category.addRestaurant(restaurant);

		restaurantRepository.save(restaurant);

		return RestaurantDto.RestaurantWriteResponse.builder()
			.id(restaurant.getId())
			.name(request.getName())
			.build();
	}

	// 모든 식당 삭제
	@Transactional
	public void truncate() {
		restaurantRepository.deleteAll();
	}

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

	public void deleteRestaurant(Long restaurantId) {
		restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		restaurantRepository.deleteById(restaurantId);
	}

	@Transactional
	public RestaurantDto.RestaurantWriteResponse modifyRestaurant(Long restaurantId,
		RestaurantDto.RestaurantWriteRequest request) {
		Restaurant restaurant = restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

		restaurant.setName(request.getName());
		restaurant.setDetails(request.getDetails());
		restaurant.setAverageRate(request.getAverageRate());
		restaurant.setImageUrl(request.getImageUrl());
		restaurant.setContact(request.getContact());
		restaurant.setAddress(request.getAddress());
		restaurant.setLat(request.getLat());
		restaurant.setLon(request.getLon());
		restaurant.setRunningState(request.getRunningState());
		restaurant.setSummarizedReview(request.getSummarizedReview());
		restaurant.setRestaurantCategory(restaurantCategoryRepository.findById(request.getCategoryId())
			.orElseThrow(() -> new IllegalArgumentException("Category not found")));

		return RestaurantDto.RestaurantWriteResponse.builder()
			.id(restaurant.getId())
			.name(request.getName())
			.build();
	}

	//    // ✅ 레스토랑 저장 메서드 추가
	//    @Transactional
	//    public Restaurant save(Restaurant restaurant) {
	//        return restaurantRepository.save(restaurant);
	//    }
	//
	//    // ✅ 모든 레스토랑 조회
	//    @Transactional(readOnly = true)
	//    public List<Restaurant> findAllRestaurants() {
	//        return restaurantRepository.findAll();
	//    }

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

	public Restaurant findById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
	}
}
