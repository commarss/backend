package com.ll.commars.domain.restaurant.category.service;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourDto;
import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.entity.RestaurantCategory;
import com.ll.commars.domain.restaurant.category.repository.RestaurantCategoryRepository;
import com.ll.commars.domain.restaurant.menu.dto.RestaurantMenuDto;
import com.ll.commars.domain.restaurant.menu.repository.RestaurantMenuRepository;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantCategoryService {
    private final RestaurantCategoryRepository restaurantCategoryRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantCategoryDto.RestaurantCategoryInfo writeCategory(
            RestaurantCategoryDto.RestaurantCategoryEnrollRequest request
    ) {
        RestaurantCategory restaurantCategory = RestaurantCategory.builder()
                .name(request.getName())
                .build();

        restaurantCategoryRepository.save(restaurantCategory);

        return RestaurantCategoryDto.RestaurantCategoryInfo.builder()
                .id(restaurantCategory.getId())
                .name(restaurantCategory.getName())
                .build();
    }

    public RestaurantCategoryDto.ShowAllCategoriesResponse getCategories() {
        List<RestaurantCategoryDto.RestaurantCategoryInfo> categories = restaurantCategoryRepository.findAll().stream()
                .map(restaurantCategory -> RestaurantCategoryDto.RestaurantCategoryInfo.builder()
                        .id(restaurantCategory.getId())
                        .name(restaurantCategory.getName())
                        .build())
                .toList();

        return RestaurantCategoryDto.ShowAllCategoriesResponse.builder()
                .categories(categories)
                .build();
    }

    @Transactional
    public RestaurantCategoryDto.ShowAllRestaurantsByCategoryResponse getRestaurantByCategory(Long categoryId) {
        // 카테고리 ID로 카테고리 찾기
        RestaurantCategory restaurantCategory = restaurantCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        List<RestaurantDto.RestaurantInfo> restaurants = restaurantRepository.findByRestaurantCategoryId(categoryId)
                .stream()
                .map(restaurant -> RestaurantDto.RestaurantInfo.builder()
                        .id(restaurant.getId())
                        .name(restaurant.getName())
                        .details(restaurant.getDetails())
                        .address(restaurant.getAddress())
                        .averageRate(restaurant.getAverageRate())
                        .imageUrl(restaurant.getImageUrl())
                        .contact(restaurant.getContact())
                        .lat(restaurant.getLat())
                        .lon(restaurant.getLon())
                        .runningState(restaurant.getRunningState())
                        .summarizedReview(restaurant.getSummarizedReview())
                        .categoryId(restaurant.getRestaurantCategory().getId())
                        .restaurantMenus(restaurantMenuRepository.findByRestaurantId(restaurant.getId()).stream()
                                .map(restaurantMenu -> RestaurantMenuDto.MenuInfo.builder()
                                        .name(restaurantMenu.getName())
                                        .price(restaurantMenu.getPrice())
                                        .imageUrl(restaurantMenu.getImageUrl())
                                        .build())
                                .toList())
                        .reviews(restaurant.getReviews().stream()
                                .map(review -> ReviewDto.ReviewInfo.builder()
                                        .restaurantId(review.getRestaurant().getId())
                                        .userId(review.getUser().getId())
                                        .id(review.getId())
                                        .name(review.getName())
                                        .body(review.getBody())
                                        .rate(review.getRate())
                                        .build())
                                .toList())
                        .businessHours(restaurant.getBusinessHours().stream()
                                .map(businessHour -> BusinessHourDto.BusinessHourInfo.builder()
                                        .id(businessHour.getId())
                                        .dayOfWeek(businessHour.getDayOfWeek())
                                        .openTime(businessHour.getOpenTime())
                                        .closeTime(businessHour.getCloseTime())
                                        .build())
                                .toList())
                        .build())
                .toList();

        return RestaurantCategoryDto.ShowAllRestaurantsByCategoryResponse.builder()
                .id(restaurantCategory.getId())
                .categoryName(restaurantCategory.getName())
                .restaurants(restaurants)
                .build();
    }

    public void truncate() {
        restaurantCategoryRepository.deleteAll();
    }
}
