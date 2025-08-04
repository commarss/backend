package com.ll.commars.domain.review.reviewDoc.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.review.reviewDoc.document.ReviewDoc;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviewsDocs")
@RequiredArgsConstructor
public class ApiV1ReviewDocController {

	private final ReviewDocService reviewDocService;

	//    @PostMapping("/write")
	//    public RsData<ReviewDoc> write(
	//            @RequestBody @Valid ReviewsDocWriteRequest request
	//    ){
	//        ReviewDoc reviewDoc = reviewDocService.write(request.content, request.rate);
	//        return new RsData<>("201", "리뷰 작성 성공", reviewDoc);
	//    }

	//    record ReviewsDocWriteRequest(
	//            @NotBlank String content,
	//            @NotBlank Integer rate
	//    ) {}

	@GetMapping("/search")
	public List<ReviewDoc> search(@RequestParam("keyword") String keyword) {
		List<ReviewDoc> results = reviewDocService.searchByKeyword(keyword);
		System.out.println("results = " + results);

		return results;
	}
}
