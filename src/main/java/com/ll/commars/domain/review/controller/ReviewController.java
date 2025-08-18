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
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.domain.review.entity.ReviewDoc;
import com.ll.commars.domain.review.service.ReviewCommandService;
import com.ll.commars.domain.review.service.ReviewDocService;
import com.ll.commars.domain.review.service.ReviewQueryService;
import com.ll.commars.domain.review.service.ReviewService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

	private final ReviewCommandService reviewCommandService;
	private final ReviewQueryService reviewQueryService;
	private final ReviewDocService reviewDocService;

	@PostMapping
	public ResponseEntity<ReviewCreateResponse> createReview(
		@RequestBody @Valid ReviewCreateRequest request
	) {
		ReviewCreateResponse response = reviewCommandService.createReview(request);

		return ResponseEntity.ok().body(response);
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

	@PatchMapping("/{review_id}")
	public ResponseEntity<ReviewDto.ReviewWriteResponse> modifyReview(
		@PathVariable("review_id") @NotNull Long reviewId,
		@RequestBody @Valid ReviewDto.ReviewWriteRequest request
	) {
		ReviewDto.ReviewWriteResponse response = reviewService.modifyReview(reviewId, request);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@GetMapping("/search")
	public List<ReviewDoc> search(@RequestParam("keyword") String keyword) {
		List<ReviewDoc> results = reviewDocService.searchByKeyword(keyword);
		System.out.println("results = " + results);

		return results;
	}
}
