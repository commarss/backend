package com.ll.commars.domain.restaurant.businessHour.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public interface BusinessHourData {
	DayOfWeek dayOfWeek();
	LocalTime openTime();
	LocalTime closeTime();
}
