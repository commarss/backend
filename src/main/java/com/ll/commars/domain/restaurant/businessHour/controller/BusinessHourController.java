package com.ll.commars.domain.restaurant.businessHour.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateResponse;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourUpdateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourUpdateResponse;
import com.ll.commars.domain.restaurant.businessHour.service.BusinessHourService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant/{restaurant-id}/business-hours")
public class BusinessHourController {

	private final BusinessHourService businessHourService;

	@PostMapping
	public ResponseEntity<BusinessHourBulkCreateResponse> createBusinessHours (
		@PathVariable("restaurant-id") Long restaurantId,
		@RequestBody @Valid BusinessHourBulkCreateRequest request
	) {
		BusinessHourBulkCreateResponse response = businessHourService.createBusinessHours(restaurantId, request);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/{business-hour-id}")
	public ResponseEntity<BusinessHourUpdateResponse> updateBusinessHour(
		@PathVariable("restaurant-id") Long restaurantId,
		@PathVariable("business-hour-id") Long businessHourId,
		@RequestBody @Valid BusinessHourUpdateRequest request
	) {
		BusinessHourUpdateResponse response = businessHourService.updateBusinessHour(restaurantId, businessHourId, request);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/{business-hour-id}")
	public ResponseEntity<Void> deleteBusinessHour(
		@PathVariable("restaurant-id") Long restaurantId,
		@PathVariable("business-hour-id") Long businessHourId
	) {
		businessHourService.deleteBusinessHour(restaurantId, businessHourId);

		return ResponseEntity.ok().build();
	}
}
