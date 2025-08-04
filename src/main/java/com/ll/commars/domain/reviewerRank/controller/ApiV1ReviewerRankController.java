package com.ll.commars.domain.reviewerRank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.reviewerRank.dto.ReviewerRankResponse;
import com.ll.commars.domain.reviewerRank.service.ReviewerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviewer-rank")
@RequiredArgsConstructor
public class ApiV1ReviewerRankController {

	private final ReviewerService reviewerService;

	@GetMapping("/top10")
	public ResponseEntity<ReviewerRankResponse> getTopReviewers() {
		ReviewerRankResponse reviewerRanks = reviewerService.getTopReviewers();
		return ResponseEntity.ok(reviewerRanks);
	}
}
