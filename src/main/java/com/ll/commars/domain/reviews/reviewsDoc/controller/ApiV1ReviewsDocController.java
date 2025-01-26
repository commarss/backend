package com.ll.commars.domain.reviews.reviewsDoc.controller;

import com.ll.commars.domain.reviews.reviewsDoc.document.ReviewsDoc;
import com.ll.commars.domain.reviews.reviewsDoc.service.ReviewsDocService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviewsDocs")
@RequiredArgsConstructor
public class ApiV1ReviewsDocController {
    private final ReviewsDocService reviewsDocService;

    @PostMapping("/write")
    public RsData<ReviewsDoc> write(
            @RequestBody @Valid PostDocWriteRequest request
    ){
        ReviewsDoc reviewsDoc = reviewsDocService.write(request.content, request.rate);
        return new RsData<>("201", "리뷰 작성 성공", reviewsDoc);
    }

    record PostDocWriteRequest(
            @NotBlank String content,
            @NotBlank Integer rate
    ) {}

    @GetMapping("/search")
    public List<ReviewsDoc> search(@RequestParam("keyword") String keyword) {
        return reviewsDocService.searchByKeyword(keyword);
    }
}
