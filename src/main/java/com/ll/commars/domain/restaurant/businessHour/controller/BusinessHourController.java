package com.ll.commars.domain.restaurant.businessHour.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateRequest;
import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourBulkCreateResponse;
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
}
