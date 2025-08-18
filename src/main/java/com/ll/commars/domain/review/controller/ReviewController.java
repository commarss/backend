package com.ll.commars.domain.review.controller;

import java.util.List;

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
import com.ll.commars.domain.review.dto.ReviewUpdateRequest;
import com.ll.commars.domain.review.entity.ReviewDoc;
import com.ll.commars.domain.review.service.ReviewService;
import com.ll.commars.domain.review.service.ReviewDocService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

	@DeleteMapping("/{review_id}")
	public ResponseEntity<String> deleteReview(
		@PathVariable("review_id") @NotNull Long reviewId
	) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity
			.status(200)
			.body("리뷰 삭제 성공");
	}

	@GetMapping("/search")
	public List<ReviewDoc> search(@RequestParam("keyword") String keyword) {
		List<ReviewDoc> results = reviewDocService.searchByKeyword(keyword);
		System.out.println("results = " + results);

		return results;
	}
}
