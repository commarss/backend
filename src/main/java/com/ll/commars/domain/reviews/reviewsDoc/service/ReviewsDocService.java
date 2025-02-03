package com.ll.commars.domain.reviews.reviewsDoc.service;

import com.ll.commars.domain.reviews.reviewsDoc.document.ReviewsDoc;
import com.ll.commars.domain.reviews.reviewsDoc.repository.ReviewsDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewsDocService {
    private final ReviewsDocRepository reviewsDocRepository;

    public ReviewsDoc write(String content, Integer rate) {
        ReviewsDoc reviewsDoc = ReviewsDoc.builder()
                .content(content)
                .rate(rate)
                .build();

        return reviewsDocRepository.save(reviewsDoc);
    }

    public void truncate() {
        reviewsDocRepository.deleteAll();
    }

    public List<ReviewsDoc> searchByKeyword(String keyword) {
        return reviewsDocRepository.searchByKeyword(keyword);
    }
}
