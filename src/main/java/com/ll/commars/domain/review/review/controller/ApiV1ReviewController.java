package com.ll.commars.domain.review.review.controller;

import com.ll.commars.domain.review.review.entity.Review;
import com.ll.commars.domain.review.review.service.ReviewService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ApiV1ReviewController {
    private final ReviewService reviewService;
    @PostMapping("/write")
    public RsData<Review> write(
            @RequestBody @Valid ReviewsWriteRequest request
    ){
        Review review = reviewService.write(request.name, request.body, request.rate);
        return new RsData<>("201", "리뷰 등록 성공", review);
    }

    record ReviewsWriteRequest(
            @NotBlank String name,
            String body,
            Integer rate

    ) {}
}
