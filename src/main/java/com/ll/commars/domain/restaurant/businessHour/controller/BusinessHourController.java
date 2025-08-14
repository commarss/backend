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
import com.ll.commars.domain.restaurant.businessHour.service.BusinessHourService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business-hours")
public class BusinessHourController {

	private final BusinessHourService businessHourService;

	@PostMapping
	public ResponseEntity<BusinessHourBulkCreateResponse> createBusinessHours(
		@RequestBody @Valid BusinessHourBulkCreateRequest request
	) {
		BusinessHourBulkCreateResponse response = businessHourService.createBusinessHours(request);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping("/{business-hour-id}")
	public ResponseEntity<Void> updateBusinessHour(
		@PathVariable("business-hour-id") Long businessHourId,
		@RequestBody @Valid BusinessHourUpdateRequest request
	) {
		businessHourService.updateBusinessHour(businessHourId, request);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{business-hour-id}")
	public ResponseEntity<Void> deleteBusinessHour(
		@PathVariable("business-hour-id") Long businessHourId
	) {
		businessHourService.deleteBusinessHour(businessHourId);

		return ResponseEntity.ok().build();
	}
}
