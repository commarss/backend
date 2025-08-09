package com.ll.commars.domain.restaurant.restaurant.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.restaurant.entity.RestaurantDoc;
import com.ll.commars.domain.restaurant.restaurant.service.RestaurantDocService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/restaurantsDoc")
@RequiredArgsConstructor
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
	public List<RestaurantDoc> showSortByRate() {
		return restaurantDocService.showSortByRate();
	}

	@GetMapping("/nearby")
	public List<RestaurantDoc> findNearbyRestaurants(
		@RequestParam(value = "lat", defaultValue = "37.5665") Double lat,
		@RequestParam(value = "lng", defaultValue = "126.9780") Double lng,
		@RequestParam(value = "distance", defaultValue = "2.0") Double distance
	) {
		return restaurantDocService.findNearbyRestaurants(lat, lng, distance);
	}
}
