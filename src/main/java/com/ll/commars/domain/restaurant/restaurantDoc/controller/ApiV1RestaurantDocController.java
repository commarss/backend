package com.ll.commars.domain.restaurant.restaurantDoc.controller;

import com.ll.commars.domain.restaurant.restaurantDoc.document.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurantDoc.service.RestaurantDocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/restaurantsDoc", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "ApiV1RestaurantDocController", description = "식당 검색 API(ElasticSearch)")
public class ApiV1RestaurantDocController {
   private final RestaurantDocService restaurantDocService;

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
    @Operation(summary = "식당 검색 - 키워드로 주변 식당을 평점순 정렬")
    public ResponseEntity<List<RestaurantDoc>> search(
            @RequestParam("keyword") String keyword,
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon,
            @RequestParam(value = "distance", defaultValue = "50") String distance
    ) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            double latitude = Double.parseDouble(lat);
            double longitude = Double.parseDouble(lon);

            List<RestaurantDoc> results = restaurantDocService.searchByKeyword(
                    keyword.trim(),
                    latitude,
                    longitude,
                    distance + "km"
            );
            System.out.println("results = " + results);
            return ResponseEntity.ok(results);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.ok(List.of()); // 빈 결과 반환
        }
    }

    @GetMapping("/sort/rate")
    @Operation(summary = "평점순으로 정렬")
    public List<RestaurantDoc> showSortByRate() {
        return restaurantDocService.showSortByRate();
    }

    @GetMapping("/nearby")
    @Operation(summary = "주변 식당 검색")
    public List<RestaurantDoc> findNearbyRestaurants(
            @RequestParam(value = "lat", defaultValue = "37.5665") Double lat,
            @RequestParam(value = "lng", defaultValue = "126.9780") Double lng,
            @RequestParam(value = "distance", defaultValue = "2.0") Double distance
    ) {
        return restaurantDocService.findNearbyRestaurants(lat, lng, distance);
    }
}
