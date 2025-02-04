package com.ll.commars.domain.reviews.reviews.service;

import com.ll.commars.domain.reviews.reviews.entity.Reviews;
import com.ll.commars.domain.reviews.reviews.repository.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;

    public Reviews write(String name, String content, Integer rate) {
        Reviews reviews = Reviews.builder()
                .name(name)
                .content(content)
                .rate(rate)
                .build();

        return reviewsRepository.save(reviews);
    }

    public void truncate() {
        reviewsRepository.deleteAll();
    }
}
