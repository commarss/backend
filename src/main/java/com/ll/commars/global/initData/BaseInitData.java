package com.ll.commars.global.initData;

import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final ReviewDocService reviewDocService;
    private final ReviewService reviewService;

    private final RestaurantDocService restaurantDocService;
    private final RestaurantService restaurantService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            work1();
            work2();
            work3();
            work4();
        };
    }

    // ReviewsDoc 데이터 초기화
    private void work1() {
        reviewDocService.truncate();

//        reviewsDocService.write("하루 일과 정리", 2);
//        reviewsDocService.write("코딩의 즐거움", 5);
//        reviewsDocService.write("겨울 여행 계획", 4);
//        reviewsDocService.write("첫 직장 출근기", 1);
//        reviewsDocService.write("커피 원두 추천", 2);
//        reviewsDocService.write("운동 루틴 기록", 4);
//        reviewsDocService.write("영화 리뷰 - 인터스텔라", 4);
//        reviewsDocService.write("맛집 탐방기", 3);
//        reviewsDocService.write("독서 기록 - 나미야 잡화점의 기적", 5);
//        reviewsDocService.write("코딩 팁 공유", 3);
//        reviewsDocService.write("취미로 배우는 기타", 1);
//        reviewsDocService.write("반려견과의 산책", 5);
//        reviewsDocService.write("다음 프로젝트 아이디어", 5);
    }

    // RestaurantsDoc 데이터 초기화
    private void work2() {
        restaurantDocService.truncate();

//        restaurantsDocService.write("마녀 커피", "마녀 커피는 커피 전문점으로, 커피의 맛이 좋아요.", 4.5);
//        restaurantsDocService.write("피자 알볼로", "피자 알볼로는 피자 전문점으로, 피자의 맛이 좋아요.", 4.0);
//        restaurantsDocService.write("진짜 치킨", "진짜 치킨은 치킨 전문점으로, 치킨의 맛이 좋아요.", 4.0);
//        restaurantsDocService.write("매운 떡볶이", "매운 떡볶이는 떡볶이 전문점으로, 떡볶이의 맛이 좋아요.", 3.0);
    }

    // Reviews 데이터 초기화
    private void work3() {
        reviewService.truncate();

        reviewService.write("whqtker", "코딩의 즐거움", 5);
        reviewService.write("test", "겨울 여행 계획", 4);
        reviewService.write("hello", "운동 루틴 기록", 1);
        reviewService.write("1q2w3e4r", "영화 리뷰 - 인터스텔라", 4);
        reviewService.write("whqtker", "독서 기록 - 나미야 잡화점의 기적", 5);
    }

    // Restaurants 데이터 초기화
    private void work4() {
        restaurantService.truncate();

        restaurantService.write("마녀 커피", "마녀 커피는 커피 전문점으로, 커피의 맛이 좋아요.", 4.5);
        restaurantService.write("피자 알볼로", "피자 알볼로는 피자 전문점으로, 피자의 맛이 좋아요.", 4.0);
        restaurantService.write("진짜 치킨", "진짜 치킨은 치킨 전문점으로, 치킨의 맛이 좋아요.", 4.0);
        restaurantService.write("매운 떡볶이", "매운 떡볶이는 떡볶이 전문점으로, 떡볶이의 맛이 좋아요.", 3.0);
    }
}
