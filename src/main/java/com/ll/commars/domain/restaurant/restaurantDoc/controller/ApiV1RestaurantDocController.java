//package com.ll.commars.domain.restaurant.restaurantDoc.controller;
//
//import com.ll.commars.domain.restaurant.restaurantDoc.document.RestaurantDoc;
//import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//@RestController
//@RequestMapping(value = "/api/v1/restaurantsDoc", produces = APPLICATION_JSON_VALUE)
//@RequiredArgsConstructor
//@Tag(name = "ApiV1RestaurantDocController", description = "식당 검색 API(ElasticSearch)")
//public class ApiV1RestaurantDocController {
//    private final RestaurantDocService restaurantDocService;
//
//    // 사실상 ES에 데이터를 쓸 일은 없음
////    @PostMapping("/write")
////    public RsData<RestaurantsDoc> write(
////            @RequestBody @Valid RestaurantsDocWriteRequest request
////    ){
////        RestaurantsDoc restaurantsDoc = restaurantsDocService.write(request.name, request.details, request.averageRate);
////        return new RsData<>("201", "식당 등록 성공", restaurantsDoc);
////    }
//
////    record RestaurantsDocWriteRequest(
////            @NotBlank String name,
////            @NotBlank String details,
////            @NotBlank Double averageRate
////    ) {}
//
//    @GetMapping("/search")
//    @Operation(summary = "식당 검색")
//    public List<RestaurantDoc> search(@RequestParam("keyword") String keyword) {
//        return restaurantDocService.searchByKeyword(keyword);
//    }
//
//    @GetMapping("/sort/rate")
//    @Operation(summary = "평점순으로 정렬")
//    public List<RestaurantDoc> showSortByRate() {
//        return restaurantDocService.showSortByRate();
//    }
//}
