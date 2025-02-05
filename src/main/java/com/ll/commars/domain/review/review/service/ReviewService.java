package com.ll.commars.domain.review.review.service;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.repository.ReviewRepository;
import com.ll.commars.domain.user.user.entity.User;
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

    public ReviewDto.ReviewShowAllResponse getReviews() {
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

        return ReviewDto.ReviewShowAllResponse.builder()
                .reviews(reviewInfos)
                .build();
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        reviewRepository.deleteById(reviewId);
    }
}
