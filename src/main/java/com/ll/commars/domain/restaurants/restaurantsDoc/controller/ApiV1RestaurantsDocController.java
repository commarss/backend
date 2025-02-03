package com.ll.commars.domain.restaurants.restaurantsDoc.controller;

import com.ll.commars.domain.restaurants.restaurantsDoc.document.RestaurantsDoc;
import com.ll.commars.domain.restaurants.restaurantsDoc.service.RestaurantsDocService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurantsDocs")
@RequiredArgsConstructor
public class ApiV1RestaurantsDocController {
    private final RestaurantsDocService restaurantsDocService;

    // 사실상 ES에 데이터를 쓸 일은 없음
//    @PostMapping("/write")
//    public RsData<RestaurantsDoc> write(
//            @RequestBody @Valid RestaurantsDocWriteRequest request
//    ){
//        RestaurantsDoc restaurantsDoc = restaurantsDocService.write(request.name, request.details, request.averageRate);
//        return new RsData<>("201", "식당 등록 성공", restaurantsDoc);
//    }

//    record RestaurantsDocWriteRequest(
//            @NotBlank String name,
//            @NotBlank String details,
//            @NotBlank Double averageRate
//    ) {}

    @GetMapping("/search")
    public List<RestaurantsDoc> search(@RequestParam("keyword") String keyword) {
        return restaurantsDocService.searchByKeyword(keyword);
    }

    // 추후 rename
    @GetMapping("/show/sortByRate")
    public List<RestaurantsDoc> showSortByRate() {
        return restaurantsDocService.showSortByRate();
    }
}
