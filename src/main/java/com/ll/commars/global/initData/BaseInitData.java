package com.ll.commars.global.initData;

import com.ll.commars.domain.reviews.reviewsDoc.service.ReviewsDocService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    private final ReviewsDocService reviewsDocService;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            work1();
            work2();
        };
    }

    // ReviewsDoc 데이터 초기화
    private void work1() {
        reviewsDocService.truncate();

        reviewsDocService.write("하루 일과 정리", 2);
        reviewsDocService.write("코딩의 즐거움", 5);
        reviewsDocService.write("겨울 여행 계획", 4);
        reviewsDocService.write("첫 직장 출근기", 1);
        reviewsDocService.write("커피 원두 추천", 2);
        reviewsDocService.write("운동 루틴 기록", 4);
        reviewsDocService.write("영화 리뷰 - 인터스텔라", 4);
        reviewsDocService.write("맛집 탐방기", 3);
        reviewsDocService.write("독서 기록 - 나미야 잡화점의 기적", 5);
        reviewsDocService.write("코딩 팁 공유", 3);
        reviewsDocService.write("취미로 배우는 기타", 1);
        reviewsDocService.write("반려견과의 산책", 5);
        reviewsDocService.write("다음 프로젝트 아이디어", 5);
    }

    // Reviews 데이터 초기화
    private void work2() {
    }
}
