package com.ll.commars.domain.restaurant.restaurant.entity;

import static com.ll.commars.global.exception.ErrorCode.*;

import java.util.Arrays;
import java.util.List;
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
				category -> category.name().toLowerCase(),
				Function.identity()
			));

	private static final List<String> ALL_CATEGORY_NAMES =
		Arrays.stream(values())
			.map(Enum::name)
			.toList();

	public static RestaurantCategory fromString(String categoryName) {
		return Optional.ofNullable(CATEGORY_MAP.get(categoryName.toLowerCase()))
			.orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND, categoryName));
	}

	public static List<String> getAllCategories() {
		return ALL_CATEGORY_NAMES;
	}
}
