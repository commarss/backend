package com.ll.commars.domain.restaurants.restaurantsDoc.controller;

import com.ll.commars.domain.restaurants.restaurantsDoc.document.RestaurantsDoc;
import com.ll.commars.domain.restaurants.restaurantsDoc.service.RestaurantsDocService;
import com.ll.commars.domain.reviews.reviewsDoc.controller.ApiV1ReviewsDocController;
import com.ll.commars.domain.reviews.reviewsDoc.document.ReviewsDoc;
import com.ll.commars.domain.reviews.reviewsDoc.service.ReviewsDocService;
import com.ll.commars.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurantsDocs")
@RequiredArgsConstructor
public class ApiV1RestaurantsDocController {
    private final RestaurantsDocService restaurantsDocService;

    @PostMapping("/write")
    public RsData<RestaurantsDoc> write(
            @RequestBody @Valid RestaurantsDocWriteRequest request
    ){
        RestaurantsDoc restaurantsDoc = restaurantsDocService.write(request.name, request.details, request.averageRate);
        return new RsData<>("201", "식당 등록 성공", restaurantsDoc);
    }

    record RestaurantsDocWriteRequest(
            @NotBlank String name,
            @NotBlank String details,
            @NotBlank Double averageRate
    ) {}

    @GetMapping("/search")
    public List<RestaurantsDoc> search(@RequestParam("keyword") String keyword) {
        return restaurantsDocService.searchByKeyword(keyword);
    }
}
