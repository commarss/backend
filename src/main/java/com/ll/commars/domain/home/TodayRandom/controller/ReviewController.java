package com.ll.commars.domain.home.TodayRandom.controller;

import com.ll.commars.domain.home.TodayRandom.dto.ReviewerRank;
import com.ll.commars.domain.home.TodayRandom.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // 추가

@RestController
@RequestMapping("/api/reviewers")
public class ReviewController {

    private final ReviewService reviewService;


    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 상위 10명의 리뷰어를 반환하는 API
    @GetMapping("/top10")
    public ResponseEntity<List<ReviewerRank>> getTopReviewers() {
        // ReviewService에서 TOP 10 리뷰어 데이터 가져오기
        List<ReviewerRank> topReviewers = reviewService.getTopReviewers();

        // 결과를 JSON 형태로 반환
        return ResponseEntity.ok(topReviewers);
    }
}