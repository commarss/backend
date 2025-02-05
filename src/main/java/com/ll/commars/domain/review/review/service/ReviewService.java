package com.ll.commars.domain.review.review.service;

import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.repository.ReviewRepository;
import com.ll.commars.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public void truncate() {
        reviewRepository.deleteAll();
    }

    public ReviewDto.ShowAllReviewsResponse getReviews() {
        List<Review> reviews = reviewRepository.findAll();

        List<ReviewDto.ReviewInfo> reviewInfos = reviews.stream()
                .map(review -> ReviewDto.ReviewInfo.builder()
                        .userName(review.getUser().getName())
                        .restaurantName(review.getRestaurant().getName())
                        .reviewName(review.getName())
                        .body(review.getBody())
                        .rate(review.getRate())
                        .build())
                .collect(Collectors.toList());

        return ReviewDto.ShowAllReviewsResponse.builder()
                .reviews(reviewInfos)
                .build();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public ReviewDto.ReviewWriteResponse modifyReview(Long reviewId, ReviewDto.ReviewWriteRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // 매개변수의 reviewId와 request의 userId가 일치하지 않으면 예외를 발생시킨다.
        if (!review.getUser().getId().equals(request.getUserId())) {
            throw new IllegalArgumentException("User not matched");
        }

        review.setName(request.getReviewName());
        review.setBody(request.getBody());
        review.setRate(request.getRate());

        return ReviewDto.ReviewWriteResponse.builder()
                .userName(review.getUser().getName())
                .restaurantName(review.getRestaurant().getName())
                .reviewName(review.getName())
                .body(review.getBody())
                .rate(review.getRate())
                .build();
    }
}
