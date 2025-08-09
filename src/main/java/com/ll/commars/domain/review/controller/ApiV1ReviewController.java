package com.ll.commars.domain.review.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.review.entity.Review;
import com.ll.commars.domain.review.dto.ReviewDto;
import com.ll.commars.domain.review.service.ReviewService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ApiV1ReviewController {

	private final ReviewService reviewService;

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

	@GetMapping("/showAllReviews")
	public ResponseEntity<ReviewDto.ShowAllReviewsResponse> showAllReviews(
		@RequestParam("restaurant_id") @NotNull Long restaurantId) {
		ReviewDto.ShowAllReviewsResponse response = reviewService.showAllReviews(restaurantId);
		return ResponseEntity
			.status(200)
			.body(response);
	}

	@PostMapping("/writeReview")
	public ResponseEntity<?> writeReview(
		@RequestBody Map<String, Object> body,
		@AuthenticationPrincipal UserDetails userDetails
	) {
		System.out.println("body = " + body);
		Review review = reviewService.wirteReview(body.get("restaurant_id").toString(), userDetails.getUsername(),
			body.get("name").toString(), body.get("body").toString(), Integer.parseInt(body.get("rate").toString()));
		System.out.println("review = " + review);
		return ResponseEntity.ok()
			.body("리뷰 작성 성공");
	}
}
