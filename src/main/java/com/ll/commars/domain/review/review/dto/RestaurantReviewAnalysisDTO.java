package com.ll.commars.domain.review.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class RestaurantReviewAnalysisDTO {
    private Long restaurantId;   // 레스토랑 ID
    private String restaurantName;   // 레스토랑 이름
    private int reviewCount;    // 리뷰 개수
    private double averageRating;   // 평균 평점
    private double weightedScore;   // 가중치 점수 (평점 * 리뷰 개수)

    // 여기에 필요한 메소드나 추가 필드를 넣을 수 있습니다.
    // 순위 필드 추가 (1등부터 N등까지)
    private int rank;
    private List<ReviewAnalysisDTO> reviews; // 리뷰 리스트 추가

    public RestaurantReviewAnalysisDTO(Long restaurantId, String restaurantName, int reviewCount, double averageRating, double weightedScore, int rank, List<ReviewAnalysisDTO> reviews) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
        this.weightedScore = weightedScore;
        this.rank = rank;
        this.reviews = reviews;
    }

    // Getter and Setter
}
