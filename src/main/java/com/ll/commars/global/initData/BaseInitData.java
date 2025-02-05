package com.ll.commars.global.initData;

import com.ll.commars.domain.restaurant.category.dto.RestaurantCategoryDto;
import com.ll.commars.domain.restaurant.category.service.RestaurantCategoryService;
import com.ll.commars.domain.restaurant.restaurant.dto.RestaurantDto;
import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final ReviewDocService reviewDocService;
    private final ReviewService reviewService;

    private final RestaurantDocService restaurantDocService;
    private final RestaurantService restaurantService;

    private final RestaurantCategoryService restaurantCategoryService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            // ES 초기화
            reviewsDocInit();
            restaurantDocInit();

            restaurantInit();
        };
    }

    // ReviewsDoc 데이터 초기화
    private void reviewsDocInit() {
        reviewDocService.truncate();
    }

    // RestaurantsDoc 데이터 초기화
    private void restaurantDocInit() {
        restaurantDocService.truncate();
    }

    // Restaurant 데이터 초기화
    private void restaurantInit() {
        restaurantService.truncate();

        String[] names = {"마녀커피", "피자알볼로", "스타벅스", "버거킹", "맘스터치", "서브웨이", "홍콩반점", "교촌치킨"};
        String[] details = {"분위기 좋은 카페", "맛있는 피자집", "글로벌 커피 체인", "햄버거 전문점", "치킨 버거 전문점",
                "샌드위치 전문점", "중국 음식점", "치킨 전문점"};
        String[] addresses = {"서울시 강남구", "서울시 서초구", "서울시 송파구", "서울시 마포구", "서울시 종로구"};
        String[] summarizedReviews = {"맛있고 분위기가 좋아요", "가성비가 좋아요", "서비스가 친절해요",
                "음식이 빨리 나와요", "재방문 의사 있어요"};

        RestaurantCategoryDto.ShowAllCategoriesResponse categories = restaurantCategoryService.getCategories();
        List<Long> categoriesId = categories.getCategories().stream()
                .map(RestaurantCategoryDto.RestaurantCategoryInfo::getId)
                .toList();

        Random random = new Random();

        IntStream.range(0, 10).forEach(i -> {
            RestaurantDto.RestaurantWriteRequest restaurant = RestaurantDto.RestaurantWriteRequest.builder()
                    .name(names[random.nextInt(names.length)])
                    .details(details[random.nextInt(details.length)])
                    .averageRate(3.0 + random.nextDouble() * 2.0) // 3.0-5.0 사이 랜덤 점수
                    .imageUrl(String.format("http://example.com/restaurant%d.jpg", i))
                    .contact(String.format("02-%d-%d", 1000 + random.nextInt(9000), 1000 + random.nextInt(9000)))
                    .address(addresses[random.nextInt(addresses.length)])
                    .lat(37.4967 + (random.nextDouble() - 0.5) * 0.1) // 37.4467-37.5467 사이
                    .lng(127.0498 + (random.nextDouble() - 0.5) * 0.1) // 126.9998-127.0998 사이
                    .runningState(random.nextBoolean())
                    .summarizedReview(summarizedReviews[random.nextInt(summarizedReviews.length)])
                    .categoryId(categoriesId.get(random.nextInt(categoriesId.size())))
                    .build();

            restaurantService.write(restaurant);
        });
    }

    // Reviews 데이터 초기화
    private void reviewsInit() {
        reviewService.truncate();

        String[] names = {"맛있네요", "좋아요", "괜찮아요", "별로에요", "맛없어요"};
        String[] bodies = {"맛있어요", "서비스가 좋아요", "가격이 착해요", "재방문 의사 있어요", "별로에요"};

        RestaurantDto.RestaurantShowAllResponse restaurants = restaurantService.getRestaurants();
        List<Long> restaurantIds = restaurants.getRestaurants().stream()
                .map(RestaurantDto.RestaurantInfo::getId)
                .toList();

        Random random = new Random();

        IntStream.range(0, 20).forEach(i -> {
            Long randomRestaurantId = restaurantIds.get(random.nextInt(restaurantIds.size()));

            ReviewDto.ReviewWriteRequest review = ReviewDto.ReviewWriteRequest.builder()
                    .reviewName(names[random.nextInt(names.length)])
                    .body(bodies[random.nextInt(bodies.length)])
                    .rate(1 + random.nextInt(5)) // 1-5 사이 랜덤 점수
                    .build();

            restaurantService.writeReview(randomRestaurantId, review);
        });
    }
}
