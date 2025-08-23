package com.ll.commars.domain.restaurant.businessHour.validator;

import static org.assertj.core.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.ll.commars.domain.restaurant.businessHour.dto.BusinessHourData;
import com.ll.commars.domain.restaurant.businessHour.dto.validation.ValidBusinessHourValidator;

@DisplayName("ValidBusinessHourValidator 테스트")
class ValidBusinessHourValidatorTest {

	private ValidBusinessHourValidator validator;

	@BeforeEach
	void setUp() {
		validator = new ValidBusinessHourValidator();
	}

	@ParameterizedTest
	@MethodSource("provideBusinessHourCases")
	void 영업_시간_유효성을_검증한다(LocalTime openTime, LocalTime closeTime, boolean expected) {
		// given
		BusinessHourData data = TestBusinessHourData.of(openTime, closeTime);

		// when
		// ConstraintValidatorContext 사용되지 않으므로 null
		boolean isValid = validator.isValid(data, null);

		// then
		assertThat(isValid).isEqualTo(expected);
	}

	private static Stream<Arguments> provideBusinessHourCases() {
		return Stream.of(
			Arguments.of(LocalTime.of(9, 0), LocalTime.of(18, 0), true),
			Arguments.of(LocalTime.of(23, 59), LocalTime.of(23, 59, 1), true),
			Arguments.of(LocalTime.of(18, 0), LocalTime.of(9, 0), false),
			Arguments.of(LocalTime.of(12, 0), LocalTime.of(12, 0), false),

			// null은 @NotNull을 통해 검증되므로 true
			Arguments.of(null, LocalTime.of(18, 0), true),
			Arguments.of(LocalTime.of(9, 0), null, true),
			Arguments.of(null, null, true)
		);
	}

	private record TestBusinessHourData(LocalTime openTime, LocalTime closeTime) implements BusinessHourData {
		@Override
		public DayOfWeek dayOfWeek() {
			return DayOfWeek.MONDAY;
		}

		static TestBusinessHourData of(LocalTime openTime, LocalTime closeTime) {
			return new TestBusinessHourData(openTime, closeTime);
		}
	}
}
