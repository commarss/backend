package com.ll.commars.domain.review.reviewDoc.controller;

import com.ll.commars.domain.review.reviewDoc.document.ReviewDoc;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviewsDocs")
@RequiredArgsConstructor
public class ApiV1ReviewDocController {
    private final ReviewDocService reviewDocService;

    @PostMapping("/write")
    public RsData<ReviewDoc> write(
            @RequestBody @Valid ReviewsDocWriteRequest request
    ){
        ReviewDoc reviewDoc = reviewDocService.write(request.content, request.rate);
        return new RsData<>("201", "리뷰 작성 성공", reviewDoc);
    }

    record ReviewsDocWriteRequest(
            @NotBlank String content,
            @NotBlank Integer rate
    ) {}

    @GetMapping("/search")
    public List<ReviewDoc> search(@RequestParam("keyword") String keyword) {
        return reviewDocService.searchByKeyword(keyword);
    }
}
