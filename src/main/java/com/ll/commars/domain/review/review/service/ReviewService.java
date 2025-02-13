package com.ll.commars.domain.review.review.service;

import com.ll.commars.domain.restaurant.restaurant.entity.Restaurant;
import com.ll.commars.domain.restaurant.restaurant.repository.RestaurantRepository;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantService;
import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.repository.ReviewRepository;
import com.ll.commars.domain.user.user.entity.User;
import com.ll.commars.domain.user.user.repository.UserRepository;
import com.ll.commars.domain.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;
    private final UserService userService;

    public void truncate() {
        reviewRepository.deleteAll();
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

    public ReviewDto.ShowAllReviewsResponse showAllReviews(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

        return ReviewDto.ShowAllReviewsResponse.builder()
                .reviews(reviews.stream()
                        .map(review -> ReviewDto.ReviewInfo.builder()
                                .id(review.getId())
                                .userId(review.getUser().getId())
                                .restaurantId(review.getRestaurant().getId())
                                .name(review.getName())
                                .body(review.getBody())
                                .rate(review.getRate())
                                .build())
                        .toList())
                .build();
    }

    public Review wirteReview(String restaurantId, String username, String name, String body, int rate) {
        Restaurant restaurant = restaurantService.findById(Long.valueOf(restaurantId));
        Optional<User> user = userService.findById(Long.parseLong(username));

        Review review = Review.builder()
                .name(name)
                .body(body)
                .rate(rate)
                .restaurant(restaurant)
                .user(user.orElseThrow(() -> new IllegalArgumentException("User not found")))
                .build();

        return reviewRepository.save(review);
    }
}
