package com.ll.commars.domain.restaurant.businessHour.dto.validation;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourCreateRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidBusinessHourValidator implements ConstraintValidator<ValidBusinessHour, BusinessHourCreateRequest> {
	@Override
	public boolean isValid(BusinessHourCreateRequest request, ConstraintValidatorContext context) {
		if (request.openTime() == null || request.closeTime() == null) {
			return true;
		}
		return request.openTime().isBefore(request.closeTime());
	}
}
