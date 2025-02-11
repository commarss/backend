package com.ll.commars.domain.review.reviewDoc.controller;

import com.ll.commars.domain.review.reviewDoc.document.ReviewDoc;
import com.ll.commars.domain.review.reviewDoc.service.ReviewDocService;
import com.ll.commars.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/reviewsDocs", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1ReviewDocController", description = "리뷰 CRUD API(ElasticSearch)")
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
   @Operation(summary = "리뷰 검색")
   public List<ReviewDoc> search(@RequestParam("keyword") String keyword) {
       return reviewDocService.searchByKeyword(keyword);
   }
}
