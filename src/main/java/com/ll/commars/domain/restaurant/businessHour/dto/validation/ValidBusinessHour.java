package com.ll.commars.domain.restaurant.businessHour.dto.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBusinessHourValidator.class)
public @interface ValidBusinessHour {
	String message() default "유효하지 않은 영업시간입니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
