package com.ll.commars.domain.restaurant.restaurant.entity;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ll.commars.global.exception.CustomException;

public enum RestaurantCategory {

	한식,
	중식,
	양식,
	일식,
	;

	private static final Map<String, RestaurantCategory> CATEGORY_MAP =
		Arrays.stream(values())
			.collect(Collectors.toUnmodifiableMap(
				category -> normalizeCategory(category.name()),
				Function.identity()
			));

	private static final List<String> ALL_CATEGORY_NAMES =
		Arrays.stream(values())
			.map(Enum::name)
			.toList();

	public static RestaurantCategory fromString(String categoryName) {
		if (categoryName == null || categoryName.trim().isEmpty()) {
			throw new CustomException(CATEGORY_NOT_FOUND, "카테고리 이름이 비어있습니다.");
		}

		String normalizedInput = normalizeCategory(categoryName);
		return Optional.ofNullable(CATEGORY_MAP.get(normalizedInput))
			.orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND,
				"존재하지 않는 카테고리입니다: " + categoryName));
	}

	private static String normalizeCategory(String input) {
		return input.trim().toLowerCase(Locale.ROOT);
	}

	public static List<String> getAllCategories() {
		return ALL_CATEGORY_NAMES;
	}
}
