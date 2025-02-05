package com.ll.commars.domain.reviewerRank.service;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.repository.ReviewRepository;
import com.ll.commars.domain.reviewerRank.dto.ReviewerRank;

import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewrService {
    public final UserRepository userRepository;
    public final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    // 상위 10명의 리뷰어 조회
    // ✅ 리뷰 작성 메서드 추가
    @Transactional
    public void writeReview(Long restaurantId, ReviewDto.ReviewWriteRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을 수 없습니다."));

        Review review = Review.builder()
                .name(request.getReviewName())
                .body(request.getBody())
                .rate(request.getRate())
                .user(user)
                .restaurant(restaurant)
                .build();

        reviewRepository.saveAndFlush(review); // 🔥 즉시 저장!
    }


    // ✅ 상위 10명의 리뷰어 조회 (Pageable 적용)
    public List<ReviewerRank> getTopReviewers() {
        List<ReviewerRank> topReviewers = reviewRepository.findTopReviewers(PageRequest.of(0, 10));
        System.out.println("🔹 상위 리뷰어 수: " + topReviewers.size());
        return topReviewers;
    }


    @Transactional
    public void truncate() {

        reviewRepository.deleteAll();

    }
}
