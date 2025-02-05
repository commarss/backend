package com.ll.commars.domain.reviewerRank.controller;

import com.ll.commars.domain.reviewerRank.dto.ReviewerRank;
import com.ll.commars.domain.reviewerRank.service.ReviewrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "ReviewController", description = "리뷰 관련 API")
public class ApiV1ReviewerRank {

    private final ReviewrService reviewService;

    // ✅ 상위 10명의 리뷰어 조회 (GET /api/reviews/top10)
    @GetMapping("/top10")
    @Operation(summary = "리뷰를 가장 많이 작성한 상위 10명의 유저 조회")
    public ResponseEntity<List<ReviewerRank>> getTopReviewers() {
        List<ReviewerRank> topReviewers = reviewService.getTopReviewers();
        return ResponseEntity.ok(topReviewers);
    }
}
