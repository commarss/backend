package com.ll.commars.domain.reviews.reviews.controller;

import com.ll.commars.domain.restaurants.restaurants.entity.Restaurants;
import com.ll.commars.domain.restaurants.restaurants.service.RestaurantsService;
import com.ll.commars.domain.reviews.reviews.entity.Reviews;
import com.ll.commars.domain.reviews.reviews.service.ReviewsService;
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
public class ApiV1ReviewsController {
    private final ReviewsService reviewsService;
    @PostMapping("/write")
    public RsData<Reviews> write(
            @RequestBody @Valid ReviewsWriteRequest request
    ){
        Reviews reviews = reviewsService.write(request.name, request.content, request.rate);
        return new RsData<>("201", "리뷰 등록 성공", reviews);
    }

    record ReviewsWriteRequest(
            @NotBlank String name,
            @NotBlank String content,
            Integer rate
    ) {}
}
