package com.ll.commars.domain.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.review.dto.ReviewCreateRequest;
import com.ll.commars.domain.review.dto.ReviewCreateResponse;
import com.ll.commars.domain.review.dto.ReviewSearchResponse;
import com.ll.commars.domain.review.dto.ReviewUpdateRequest;
import com.ll.commars.domain.review.service.ReviewDocService;
import com.ll.commars.domain.review.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewService reviewService;
	private final ReviewDocService reviewDocService;

	@PostMapping
	public ResponseEntity<ReviewCreateResponse> createReview(
		@RequestBody @Valid ReviewCreateRequest request
	) {
		ReviewCreateResponse response = reviewService.createReview(request);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/{review-id}")
	public ResponseEntity<Void> updateReview(
		@PathVariable("review-id") Long reviewId,
		@RequestBody @Valid ReviewUpdateRequest request
	) {
		reviewService.updateReview(reviewId, request);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{review-id}")
	public ResponseEntity<Void> deleteReview(
		@PathVariable("review-id") Long reviewId
	) {
		reviewService.deleteReview(reviewId);

		return ResponseEntity.ok().build();
	}

	@GetMapping("/search")
	public ResponseEntity<ReviewSearchResponse> searchByKeyword(
		@RequestParam("keyword") String keyword
	) {
		ReviewSearchResponse response = reviewDocService.searchByKeyword(keyword);

		return ResponseEntity.ok().body(response);
	}
}
