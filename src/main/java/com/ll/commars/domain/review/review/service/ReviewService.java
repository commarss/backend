package com.ll.commars.domain.review.review.service;

import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review write(String name, String body, Integer rate) {
        Review review = Review.builder()
                .name(name)
                .body(body)
                .rate(rate)
                .build();

        return reviewRepository.save(review);
    }

    public void truncate() {
        reviewRepository.deleteAll();
    }
}
