package com.ll.commars.domain.restaurant.businessHour.dto.validation;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourData;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidBusinessHourValidator implements ConstraintValidator<ValidBusinessHour, BusinessHourData> {
	@Override
	public boolean isValid(BusinessHourData businessHourData, ConstraintValidatorContext context) {
		if (businessHourData.openTime() == null || businessHourData.closeTime() == null) {
			return true;
		}
		return businessHourData.openTime().isBefore(businessHourData.closeTime());
	}
}
