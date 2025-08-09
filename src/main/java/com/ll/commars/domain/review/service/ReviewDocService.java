package com.ll.commars.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ll.commars.domain.review.entity.ReviewDoc;
import com.ll.commars.domain.review.repository.elasticsearch.ReviewDocRepository;

import lombok.RequiredArgsConstructor;

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
