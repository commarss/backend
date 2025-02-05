package com.ll.commars.domain.review.review.controller;

import com.ll.commars.domain.review.review.dto.ReviewDto;
import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ApiV1ReviewController {
    private final ReviewService reviewService;

    // 모든 리뷰 조회
    @GetMapping("/")
    public RsData<ReviewDto.ReviewShowAllResponse> getReviews() {
        ReviewDto.ReviewShowAllResponse response = reviewService.getReviews();
        return new RsData<>("200", "모든 리뷰 조회 성공", response);
    }

    // 특정 리뷰 삭제(완료)
    @DeleteMapping("/{review_id}")
    public RsData<String> deleteReview(
            @PathVariable("review_id") @NotNull Long reviewId
    ){
        reviewService.deleteReview(reviewId);
        return new RsData<>("200", "리뷰 삭제 성공", "리뷰 삭제 성공!");
    }

    // 특정 리뷰 수정
    @PatchMapping("/{review_id}")
    public RsData<ReviewDto.ReviewWriteResponse> modifyReview(
            @PathVariable("review_id") @NotNull Long reviewId,
            @RequestBody @Valid ReviewDto.ReviewWriteRequest request
    ){
        ReviewDto.ReviewWriteResponse response = reviewService.modifyReview(reviewId, request);
        return new RsData<>("200", "리뷰 수정 성공", response);
    }
}
