package com.ll.commars.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ll.commars.domain.review.dto.ReviewSearchResponse;
import com.ll.commars.domain.review.entity.ReviewDoc;
import com.ll.commars.domain.review.repository.elasticsearch.ReviewDocRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewDocService {

	private final ReviewDocRepository reviewDocRepository;

	public ReviewSearchResponse searchByKeyword(String keyword) {
		List<ReviewDoc> reviewDocs = reviewDocRepository.searchByKeyword(keyword);

		return ReviewSearchResponse.from(reviewDocs);
	}
}
