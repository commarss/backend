package com.ll.commars.domain.review.reviewDoc.service;

import com.ll.commars.domain.review.reviewDoc.document.ReviewDoc;
import com.ll.commars.domain.review.reviewDoc.repository.ReviewDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewDocService {
    private final ReviewDocRepository reviewDocRepository;

    public ReviewDoc write(String content, Integer rate) {
        ReviewDoc reviewDoc = ReviewDoc.builder()
                .name(content)
                .rate(rate)
                .build();

        return reviewDocRepository.save(reviewDoc);
    }

    public void truncate() {
        reviewDocRepository.deleteAll();
    }

    public List<ReviewDoc> searchByKeyword(String keyword) {
        return reviewDocRepository.searchByKeyword(keyword);
    }
}
